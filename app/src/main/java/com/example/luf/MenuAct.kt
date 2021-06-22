package com.example.luf

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MenuAct : AppCompatActivity()  {


    private lateinit var appBarConfiguration: AppBarConfiguration
    var prefs: SharedPreferences? = null

    

override  fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu, menu)
    return super.onCreateOptionsMenu(menu)
}

    private fun toPrefActivity()
    {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==R.id.settingsItem)
        {
            toPrefActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("com.example.luf", MODE_PRIVATE);
        setTheme(R.style.Theme_LUF_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.nav_home,
                        R.id.nav_map,
                        R.id.nav_contacts,
                        R.id.nav_profile,
                        R.id.nav_chat
                ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean
    {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume()
    {
        super.onResume()
        if (prefs!!.getBoolean("firstrun", true) || FirebaseAuth.getInstance().currentUser==null) {

            startActivity(Intent(this@MenuAct, RegisterUser::class.java))
            prefs!!.edit().putBoolean("firstrun", false).commit();
        }
    }

    override fun onPause()
    {
        super.onPause()

    }



}
