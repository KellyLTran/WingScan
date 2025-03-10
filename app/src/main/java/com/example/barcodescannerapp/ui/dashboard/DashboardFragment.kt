package com.example.barcodescannerapp.ui.dashboard

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.barcodescannerapp.ExcelReader
import com.example.barcodescannerapp.R
import com.example.barcodescannerapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    // creating variables for listview
    lateinit var productLV: ListView

    // creating array adapter for listview
    lateinit var listAdapter: ArrayAdapter<String>

    // creating array list for listview
    lateinit var productList: ArrayList<String>;

    // creating variable for searchview
    lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        // initializing variables of list view with their ids.
        productLV = binding.idLVProducts
        searchView = binding.idSV

        productList = ArrayList()

        // Read brand names from Excel and add them to productList
        val excelReader = ExcelReader(requireContext())
        productList.addAll(excelReader.readBrandNames())

        // initializing list adapter and setting layout for each list view item and adding array list to it
        listAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.list_item,
            productList
        )

        // setting list adapter to our list view.
        productLV.adapter = listAdapter

        // adding on query listener for our search view.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (productList.contains(query)) {
                    listAdapter.filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "No Product Found..", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })

        // clicking on items in the ListView
        productLV.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = listAdapter.getItem(position)
            if (selectedItem != null) {
                val action = DashboardFragmentDirections.actionNavigationDashboardToNavigationProductinfo(selectedItem, "dashboard")
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
