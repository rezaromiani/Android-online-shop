package com.example.nikestore.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.nikestore.R
import com.example.nikestore.common.NikeCompletable
import com.example.nikestore.common.NikeFragment
import com.example.nikestore.databinding.FragmentSignUpBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : NikeFragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun validation(code: (username: String, password: String) -> Unit) {
        val username = binding.signUpEmailEt.text
        val password = binding.signUpPasswordEt.text
        if (username.isNotEmpty() && password.isNotEmpty())
            code(username.toString(), password.toString())
        else
            showSnackBar("نام و ایمیل و رمز عبور خود را وارد کنید")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpBtn.setOnClickListener {
            validation { username, password ->
                authViewModel.signUp(username, password)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : NikeCompletable(compositeDisposable) {
                        override fun onComplete() {
                            requireActivity().finish()
                        }

                    })
            }
        }
        binding.loginLinkBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}