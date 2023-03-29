package site.felipeschoffen.todoapp.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.databinding.FragmentHomeBinding
import java.time.LocalDate
import java.time.LocalDateTime

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeTopLayout.homeWelcomeText.text =
            getString(R.string.hello_message, DataSource.userInfo.name)

    }
}