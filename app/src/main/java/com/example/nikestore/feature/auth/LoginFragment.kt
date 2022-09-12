package com.example.nikestore.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.nikestore.R
import com.example.nikestore.common.NikeCompletable
import com.example.nikestore.common.NikeFragment
import com.example.nikestore.databinding.FragmentLoginBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : NikeFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {

            validation { username, password ->
                authViewModel.login(username, password)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : NikeCompletable(compositeDisposable) {
                        override fun onComplete() {
                            requireActivity().finish()
                        }

                    })
            }

        }

        binding.signUpLinkBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    fun validation(code: (username: String, password: String) -> Unit) {
        val username = binding.loginEmailEt.text
        val password = binding.loginPasswordEt.text
        if (username.isNotEmpty() && password.isNotEmpty())
            code(username.toString(), password.toString())
        else
            showSnackBar("ایمیل و رمز عبور خود را وارد کنید")
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}