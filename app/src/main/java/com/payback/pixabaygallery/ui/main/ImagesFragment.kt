package com.payback.pixabaygallery.ui.main


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.payback.pixabaygallery.R
import com.payback.pixabaygallery.api.ApiConstants.Companion.DEFAULT_SEARCH_QUERY
import com.payback.pixabaygallery.data.model.Image
import com.payback.pixabaygallery.databinding.FragmentImagesBinding
import com.payback.pixabaygallery.ui.main.viewModel.ImageViewModel
import com.payback.pixabaygallery.util.EndlessScrollListener
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ImagesFragment : Fragment(), OnImageClick {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var binding: FragmentImagesBinding
    private lateinit var imageListAdapter: ImagesListAdapter
    private var searchQuery = DEFAULT_SEARCH_QUERY
    private var pageNumber = 1
    private val imageQuerySubject = PublishSubject.create<String>()
    private lateinit var endlessScrollListener: EndlessScrollListener
    private var imagesListView: RecyclerView? = null
    private var callDefaultRequest = true
    lateinit var image: Image
    lateinit var moreDetailsAlertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        imageSearchViewObserver()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_images, container, false)
        imageListAdapter = ImagesListAdapter(this)
        binding.retryBtn.setOnClickListener { imageViewModel.getImages(searchQuery, pageNumber) }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageViewModel = ViewModelProvider(this, viewModelFactory).get(ImageViewModel::class.java)
        imageViewModel.setRxSchedulers(Schedulers.io(), AndroidSchedulers.mainThread())
        initImageRecyclerView(savedInstanceState)
        initMoreDetailsAlertDialog()

        if (savedInstanceState != null) {
            callDefaultRequest = false
            searchQuery = savedInstanceState.getString("search")!!
            pageNumber = savedInstanceState.getInt("page_number")
        }

        if (callDefaultRequest) {
            imageViewModel.getImages(searchQuery, pageNumber)

        }

        imageViewModel.imagesViewStateLiveData.observe(viewLifecycleOwner, Observer {
            binding.imageViewState = it
            handleImagesList(it.imagesDataState)
            handleErrorState(it.errorState)
        })
    }

    private fun handleErrorState(errorState: Throwable?) {
        if (errorState != null) {
            Toast.makeText(activity?.applicationContext, errorState.message, Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun handleImagesList(imagesDataState: List<Image>) {
        if (imagesDataState.isNotEmpty()) {
            imageListAdapter.submitList(imagesDataState)
        }
    }

    private fun initImageRecyclerView(savedInstanceState: Bundle?) {
        imagesListView = binding.imageListView.also {
            it.adapter = imageListAdapter
            it.layoutManager = LinearLayoutManager(activity)
            if (savedInstanceState != null) {
                it.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable("layoutmanager"))
                it.scrollToPosition(savedInstanceState.getInt("last_position"))
            }

            endlessScrollListener = object : EndlessScrollListener() {
                override fun onLoadMore() {
                    pageNumber++
                    imageViewModel.getImages(searchQuery, pageNumber)
                    Timber.e(pageNumber.toString())
                }
            }
            it.addOnScrollListener(endlessScrollListener)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val menuItem: MenuItem = menu.findItem(R.id.action_search)
        val searchItem = menuItem.actionView as SearchView
        searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false
            override fun onQueryTextChange(newText: String): Boolean {
                imageQuerySubject.onNext(newText)
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    @SuppressLint("CheckResult")
    private fun imageSearchViewObserver() {
        imageQuerySubject
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap { return@switchMap Observable.just(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty()) {
                    searchQuery = it
                    pageNumber = 1
                    imageViewModel.clearList()
                    endlessScrollListener.resetValues()
                    imageViewModel.getImages(searchQuery, pageNumber, isSearch = true)
                }
            }, { error -> Timber.e(error) })
    }

    private fun initMoreDetailsAlertDialog() {
         moreDetailsAlertDialog = AlertDialog.Builder(activity!!)
             .setMessage(getString(R.string.ask_more_details))
             .setPositiveButton(android.R.string.ok) { _, _ ->
                val bundle = bundleOf("image" to image)
                callDefaultRequest = false
                findNavController().navigate(R.id.action_imagesFragment_to_imageDetailsFragment, bundle)
              }
             .setNegativeButton(android.R.string.no) {dialog,_ -> dialog.dismiss()}
            .create()
    }

    override fun onClick(image: Image) {
        this.image = image
        moreDetailsAlertDialog.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (imagesListView != null) {
            outState.putParcelable(
                "layoutmanager",
                imagesListView?.layoutManager?.onSaveInstanceState()
            )
            outState.putInt(
                "last_position",
                (imagesListView?.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            )
        }
        outState.putInt("page_number", pageNumber)
        outState.putString("search", searchQuery)
    }
}
