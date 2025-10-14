package com.russia.criminalintent

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.doOnLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.russia.criminalintent.databinding.FragmentCrimeDetailBinding
import com.russia.criminalintent.databinding.FragmentPhotoZoomBinding
import java.io.File

class PhotoZoomFragment: DialogFragment() {

    private var _binding: FragmentPhotoZoomBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is it view visible ?"
    }

    private val args: PhotoZoomFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoZoomBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            updatePhoto(args.photoName)
        }
    }

    private fun updatePhoto(photoFileName: String?) {
        if (binding.crimeImage.tag != photoFileName) {
            val photFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }
            if (photFile?.exists() == true) {
                binding.crimeImage.doOnLayout {  measuredView ->
                    val scaledBitmap = getScalingBitmap(
                        photFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.crimeImage.setImageBitmap(scaledBitmap)
                    binding.crimeImage.tag = photoFileName
                }
            } else {
                binding.crimeImage.setImageBitmap(null)
                binding.crimeImage.tag = null
            }
        }
    }
}