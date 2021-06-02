package ru.foody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.activity_main.*
import ru.foody.fragments.diary.DiaryFragment
import ru.foody.fragments.ProfileFragment
import ru.foody.fragments.recipes.RecipeListFragment
import ru.foody.fragments.products.SearchProductFragment
import ru.foody.service.DiaryService

class MainActivity : AppCompatActivity() {

    lateinit var diaryService : DiaryService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        diaryService = DiaryService(applicationContext)

        setContentView(R.layout.activity_main)

        val productSearchFragment = SearchProductFragment()
        val profileFragment = ProfileFragment()
        val recipesFragment = RecipeListFragment()
        val diaryFragment = DiaryFragment()

        setCurrentFragment(productSearchFragment)

        this.bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_products -> setCurrentFragment(productSearchFragment)
                R.id.menu_profile -> setCurrentFragment(profileFragment)
                R.id.menu_recipes -> setCurrentFragment(recipesFragment)
                R.id.menu_diary -> setCurrentFragment(diaryFragment)
            }
            true
        }
    }

    fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fl_wrapper, fragment)
        }
    }
}