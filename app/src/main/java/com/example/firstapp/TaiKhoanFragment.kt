package com.example.firstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firstapp.databinding.FragmentTaikhoanBinding

class TaiKhoanFragment : Fragment() {
    private var _binding: FragmentTaikhoanBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaikhoanBinding.inflate(inflater, container, false)
        return binding.root
    }
}
