package com.upax.aplicationupax.vista.movies.menu

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.upax.aplicationupax.R
import com.upax.aplicationupax.vista.movies.base.BaseActivity
import com.upax.aplicationupax.vista.movies.base.BaseFragment
import com.upax.aplicationupax.vista.movies.mapa_fragment.MapaFragment
import com.upax.aplicationupax.vista.movies.movie_list.MovieFragment
import kotlinx.android.synthetic.main.activity_main_navigationview.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MenuActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var viewModel : MenuViewModel

    private val peliculasF : MovieFragment by lazy {
        MovieFragment.newInstance()
    }
    private val mapaF : MapaFragment by lazy {
        MapaFragment.newInstance()
    }
    /*private val imagenesF : GigasFragment by lazy {
        GigasFragment.newInstance()
    }*/


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var currentFragment: BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigationview)

        setSupportActionBar(root_view.main_toolbar)

        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        drawerLayout = findViewById(R.id.drawer_layout)

        toogle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nav_view.setNavigationItemSelectedListener(this)
        changeFragment(FragmentShow.PELICULAS)

        mapa2.setOnClickListener {
            changeFragment(FragmentShow.MAPA)
        }
        movie_db2.setOnClickListener {
            changeFragment(FragmentShow.PELICULAS)
        }
        subir_imagen2.setOnClickListener {
            changeFragment(FragmentShow.PELICULAS)
        }

    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()

    }

    private fun changeFragment(fragmentShow: FragmentShow) {
        currentFragment = when (fragmentShow) {
            FragmentShow.PELICULAS -> peliculasF
            FragmentShow.MAPA -> mapaF
            FragmentShow.IMAGENES -> peliculasF
        }
            launchFragmentTransaction(fragmentShow)
    }

    private fun launchFragmentTransaction (fragmentShow: FragmentShow){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_content, currentFragment
            )
            .commitNow()
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            finishAffinity()
        super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.movie_db -> changeFragment(FragmentShow.PELICULAS)
            R.id.mapa -> changeFragment(FragmentShow.MAPA)
            R.id.subir_imagen -> changeFragment(FragmentShow.PELICULAS)

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)


        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    enum class FragmentShow {
        PELICULAS,
        MAPA,
        IMAGENES
    }

    fun changeFragmento(fragmentShow: FragmentShow) {
        changeFragment(fragmentShow)
    }

}