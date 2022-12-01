package com.cs371m.bookmark

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cs371m.bookmark.databinding.ActionBarBinding
import com.cs371m.bookmark.databinding.ActivityMainBinding
import com.cs371m.bookmark.ui.hot.HotAdapter
import com.cs371m.bookmark.ui.onePost.OnePost
import com.cs371m.bookmark.ui.search.searchPost
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

class MainActivity2 : AppCompatActivity() {

    companion object {
        var globalDebug = false // default: false
        lateinit var allBook: String
        lateinit var allUser: String
    }

    private var actionBarBinding: ActionBarBinding? = null
    private val viewModel : MainViewModel by viewModels()

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            viewModel.updateUser()
        }

    // An Android nightmare
    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    // https://stackoverflow.com/questions/7789514/how-to-get-activitys-windowtoken-without-view
    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    private fun initDebug() {
        if(globalDebug) {
            assets.list("")?.forEach {
                Log.d(javaClass.simpleName, "Asset file: $it" )
            }
            allBook = assets.open("allBook.txt").bufferedReader().use {
                it.readText()
            }
            Log.d(javaClass.simpleName, "allBook file: $allBook" )

            allUser = assets.open("allUser.txt").bufferedReader().use {
                it.readText()
            }
            Log.d(javaClass.simpleName, "allUser file: $allUser" )

        }
    }

    private fun initActionBar(actionBar: ActionBar) {
        // Disable the default and enable the custom
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBarBinding = ActionBarBinding.inflate(layoutInflater)
        // Apply the custom view
        actionBar.customView = actionBarBinding?.root
    }

    // Check out addTextChangedListener
    private fun actionBarSearch() {

        actionBarBinding?.actionSearch?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(applicationContext, searchPost::class.java)
                intent.apply {
                    putExtra("query", query)
                    // putExtra(hotStars, item.averageRate)
                    // putExtra(hotTitle, item.title)
                    // putExtra(hotAuthor, item.author)
                }
                startActivity(intent)
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        /* actionBarBinding?.actionSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()) {
                    hideKeyboard()
                }
                // viewModel.setSearchTerm(s.toString())
            }
        })*/
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.signOut()

        AuthInit(viewModel, signInLauncher)

        supportActionBar?.let{
            initActionBar(it)
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_hot, R.id.navigation_rate, R.id.navigation_collection, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        initDebug()
        actionBarSearch()
        viewModel.netRefresh()
    }
}