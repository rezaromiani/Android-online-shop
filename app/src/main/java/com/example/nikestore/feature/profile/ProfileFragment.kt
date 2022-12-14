package com.example.nikestore.feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nikestore.R
import com.example.nikestore.databinding.FragmentHomeBinding
import com.example.nikestore.databinding.FragmentProfileBinding
import com.example.nikestore.feature.auth.AuthActivity
import com.example.nikestore.feature.favorite.FavoriteProductActivity
import com.example.nikestore.feature.order.OrderHistoryActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by viewModel()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoriteLink.setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteProductActivity::class.java))
        }
        binding.orderHistoryLink.setOnClickListener {
            startActivity(Intent(requireContext(), OrderHistoryActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        checkAuthState()
    }

    private fun checkAuthState() {
        if (profileViewModel.isSignIN) {
            binding.authBtn.text = getString(R.string.signOut)
            binding.authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_out, 0)
            binding.usernameTv.text = profileViewModel.username
            binding.authBtn.setOnClickListener {
                profileViewModel.signOut()
                checkAuthState()
            }
        } else {
            binding.authBtn.text = getString(R.string.signIn)
            binding.authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            binding.usernameTv.text = getString(R.string.guest_user)
            binding.authBtn.setOnClickListener {
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }
        }
    }
}