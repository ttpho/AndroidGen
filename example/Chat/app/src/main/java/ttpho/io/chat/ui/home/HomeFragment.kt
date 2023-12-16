package ttpho.io.chat.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ttpho.io.chat.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val imageView: ImageView = binding.imageView

        // DON'T
/*
        Glide.with(this)
            .load(Uri.parse("file:///android_asset/images/flower.png"))
            .into(imageView)
*/
        // SHOULD
        Glide.with(this)
            .load(Uri.parse(BuildAssetManager.IMAGES_FLOWER))
            .into(imageView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}