package com.chiclaim.play.android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Fragment 基类。为了避免被直接使用，需要声明为 abstract
 *
 * @author by chiclaim@google.com
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    val fragmentProvider: ScopeViewModel.FragmentViewModelProvider by lazy {
        ScopeViewModel.FragmentViewModelProvider(this)
    }

    val activityProvider: ScopeViewModel.ActivityViewModelProvider by lazy {
        ScopeViewModel.ActivityViewModelProvider(requireActivity())
    }

    val applicationProvider: ScopeViewModel.ApplicationViewModelProvider by lazy {
        ScopeViewModel.ApplicationViewModelProvider(requireActivity().application)
    }

    private var isLoaded = false
    private var destroyViewState = false

    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 延迟加载数据。当 Fragment onResume 时候才会调用，可以在此方法中进行网络请求等操作
     */
    abstract fun lazyLoad()


    /**
     * 在 [onViewCreated] 方法中调用，可以做一些初始化操作
     */
    open fun init(view: View) {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }


    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            isLoaded = true
            lazyLoad()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
        destroyViewState = true
    }

    fun getDestroyViewStateAndReset(): Boolean {
        val d = destroyViewState
        destroyViewState = false
        return d
    }


}