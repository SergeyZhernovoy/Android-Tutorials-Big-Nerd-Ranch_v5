package com.russia.criminalintent

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.russia.criminalintent.databinding.FragmentCrimeDetailBinding
import com.russia.criminalintent.domain.Crime
import com.russia.criminalintent.domain.CrimeDetailViewModel
import com.russia.criminalintent.domain.CrimeDetailViewModelFactory
import kotlinx.coroutines.launch
import java.util.Date
import androidx.core.net.toUri
import androidx.core.view.doOnLayout
import java.io.File


class CrimeDetailFragment : Fragment() {

    private var _binding: FragmentCrimeDetailBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is it view visible ?"
        }

    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    private val selectSuspect = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let {
            parseContactSelection(it)
        }
    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        didTakePhoto: Boolean ->
        if (didTakePhoto && photoName != null) {
            crimeDetailViewModel.updateCrime { oldCrime ->
                oldCrime.copy(photoFileName = photoName)
            }
        }
    }

    private var photoName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(object: OnBackPressedCallback(true) {
                @SuppressLint("RestrictedApi")
                override fun handleOnBackPressed() {
                    if (binding.crimeTitle.text.isNullOrEmpty()) {
                        Toast
                            .makeText(binding.root.context, "This crime has an empty title!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else {
                        findNavController().popBackStack(R.id.crimeDetailFragment, true)
                    }
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }
            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    crimeDetailViewModel.crime.collect { crime ->
                        crime?.let { updateUi(it) }
                    }
                }
            }
            setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) {
                requestKey, bundle ->
                val newDate =
                    bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE, Date::class.java) as Date
                crimeDetailViewModel.updateCrime { it.copy(date = newDate) }
            }
            setFragmentResultListener(TimePickerFragment.REQUEST_KEY_TIME) {
                    requestKey, bundle ->
                val time =
                    bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME, Date::class.java) as Date
                crimeDetailViewModel.updateCrime { it.copy(date = time) }
            }
            crimeSuspect.setOnClickListener {
                selectSuspect.launch(null)
            }
            val selectSuspectIntent = selectSuspect.contract.createIntent(
                requireContext(),
                null
            )
            crimeSuspect.isEnabled = canResolveIntent(selectSuspectIntent)
            crimeCall.isEnabled = canResolveIntent(selectSuspectIntent)

            crimeCamera.setOnClickListener {
                photoName = "IMG_${Date()}.JPG"
                val photoFile = File(requireContext().applicationContext.filesDir, photoName)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.russia.criminalintent.fileprovider",
                    photoFile
                )
                takePhoto.launch(photoUri)
            }
            val captureImageIntent = takePhoto.contract.createIntent(
                requireContext(),
                "".toUri()
            )
            crimeCamera.isEnabled = canResolveIntent(captureImageIntent)
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                menuInflater.inflate(R.menu.fragment_crime_detail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_crime -> {
                        deleteCrime()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun deleteCrime() {
        viewLifecycleOwner.lifecycleScope.launch {
            crimeDetailViewModel.crime.collect { crime ->
                crime?.run {
                    crimeDetailViewModel.deleteCrime(crime)
                    findNavController().popBackStack(R.id.crimeDetailFragment, true)
                }
            }
        }
    }

    private fun updateUi(crime: Crime) {
        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = DateFormat.format("EEEEEE, MM d, yyyy"  ,crime.date)
            crimeTime.text = DateFormat.format("HH:mm"  ,crime.date)
            crimeSolved.isChecked = crime.isSolved

            crimeSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.crime_suspect_text)
            }
            if (crime.phone.isNotEmpty())
                crimeCall.text = getString(R.string.call_info, crime.phone, crime.suspect)

            crimeReport.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject))
                }.also { intent ->
                    val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report))
                    startActivity(chooserIntent)
                }
            }
            crimeCall.setOnClickListener {
                Intent(Intent.ACTION_DIAL).apply {
                    data = "tel: ${crime.phone}".toUri()
                }.also {
                    startActivity(it)
                }
            }

            crimeDate.setOnClickListener {
                findNavController().navigate(CrimeDetailFragmentDirections.selectDate(crime.date))
            }
            crimeTime.setOnClickListener {
                findNavController().navigate(CrimeDetailFragmentDirections.selectTime(crime.date))
            }
            crimePhoto.setOnClickListener {
                crime.photoFileName?.let {
                    findNavController().navigate(CrimeDetailFragmentDirections.selectZoomPhoto(it))
                }
            }
            updatePhoto(crime.photoFileName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCrimeReport(crime: Crime): String {
        val solvedString = if (crime.isSolved)
            getString(R.string.crime_report_solved)
        else
            getString(R.string.crime_report_unsolved)

        val dateString = DateFormat
            .format("EEEEEE, MM d, yyyy", crime.date)
            .toString()

        val suspectText = if (crime.suspect.isBlank())
            getString(R.string.crime_report_no_suspect)
         else
             getString(R.string.crime_report_suspect, crime.suspect)

        return getString(R.string.crime_report,
            crime.title, dateString, solvedString, suspectText)
    }

    private fun parseContactSelection(contactUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID)
        val contentResolver = requireActivity().contentResolver
        val queryCursor = contentResolver
            .query(contactUri, queryFields, null, null, null)
        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val newSuspect = cursor.getString(0)
                var newPhone = ""
                val id = cursor.getString(1)
                id?.let {
                    val queryPhoneCursor = contentResolver
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                            arrayOf(it),
                            null)
                    queryPhoneCursor?.use { phoneCursor ->
                        if (queryPhoneCursor.moveToFirst()) {
                            val index = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            newPhone = phoneCursor.getString(index)
                        }
                    }
                }
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(suspect = newSuspect, phone = newPhone)
                }
            }
        }
    }

    private fun updatePhoto(photoFileName: String?) {
        if (binding.crimePhoto.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }
            if (photoFile?.exists() == true) {
                binding.crimePhoto.doOnLayout {  measuredView ->
                    val scaledBitmap = getScalingBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.crimePhoto.setImageBitmap(scaledBitmap)
                    binding.crimePhoto.tag = photoFileName
                    binding.crimePhoto.contentDescription = getString(R.string.crime_photo_image_description)
                }
            } else {
                binding.crimePhoto.setImageBitmap(null)
                binding.crimePhoto.tag = null
                binding.crimePhoto.contentDescription = getString(R.string.crime_photo_no_image_description)
            }
        }
    }

    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolvedActivity != null
    }

}