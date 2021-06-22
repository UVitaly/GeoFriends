package com.example.luf.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.luf.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView


class HomeFragment : Fragment()
{

    private lateinit var homeViewModel: HomeViewModel
    var temp : ArrayList<String>? = ArrayList()
    var counter: Int? = 0
    var arrText : ArrayList<TextView> = ArrayList()
    var arrImage : ArrayList<CircleImageView> = ArrayList()
    var arrTextFavorite : ArrayList<TextView> = ArrayList()
    var arrImageFavorite : ArrayList<CircleImageView> = ArrayList()
    var ll: LinearLayout? = null
    var bFavoriteStatus : Boolean = false
    var llScrollViewFavorites : LinearLayout? = null
    var i : Int = 1
    var paramsImage: FrameLayout.LayoutParams = FrameLayout.LayoutParams(100, 100)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root

    }

    override fun onResume()
    {
        super.onResume()
        val mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        var query = mFirebaseDatabaseReference.child("Contacts")
        var queryNewText =  mFirebaseDatabaseReference.child("User")


        val textEventListener:ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(FirebaseAuth.getInstance().currentUser!=null)
                {
                    var tv: TextView = TextView(requireContext())
                    var tvFavorite: TextView = TextView(requireContext())
                    for (ds2 in dataSnapshot.children) {
                        if ( ds2.key == temp!![counter!!] ) {
                            val lLayout : LinearLayout = requireActivity().findViewById(R.id.llScrollView)
                            tv.text = ds2.child("fullName").value.toString()
                            tvFavorite.text = ds2.child("fullName").value.toString()
                            arrText!!.add(tv)
                            arrTextFavorite!!.add(tvFavorite)
                            ll = LinearLayout(requireContext())
                            ll!!.addView(arrImage[counter!!])
                            ll!!.addView(arrText[counter!!])
                            if(bFavoriteStatus==true)
                            {
                                llScrollViewFavorites= LinearLayout(requireContext())
                                var lLayoutFavorite : LinearLayout = requireActivity().findViewById(R.id.ll_ScrollViewFavorites)
                                llScrollViewFavorites!!.addView(arrImageFavorite[counter!!])
                                llScrollViewFavorites!!.addView(arrTextFavorite[counter!!])
                                lLayoutFavorite.addView(llScrollViewFavorites)
                                bFavoriteStatus=false
                            }
                            lLayout.addView(ll)
                        }
                    }
                    counter= counter!! + 1
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(FirebaseAuth.getInstance().currentUser!=null)
                {
                    for (ds in dataSnapshot.children) {
                        if (ds.child("idSecondClient").value == FirebaseAuth.getInstance().currentUser!!.uid ) {
                            if(ds.child("isFavorite").value.toString()==i.toString())
                            {
                                val svFavorites:ScrollView = requireActivity().findViewById(R.id.horizontalScrollView2)
                                svFavorites.setBackgroundResource(R.drawable.corners)
                                bFavoriteStatus = true
                            }
                            val rImage: CircleImageView = CircleImageView(requireContext())
                            val rImageFavorite: CircleImageView = CircleImageView(requireContext())
                            val svLast:ScrollView = requireActivity().findViewById(R.id.horizontalScrollView1)
                            svLast.setBackgroundResource(R.drawable.corners)
                            temp!!.add(ds.child("idFirstClient").value.toString())
                            rImage.setImageResource(R.drawable.ic_menu_camera)
                            rImageFavorite.setImageResource(R.drawable.ic_menu_camera)
                            arrImage.add(rImage)
                            arrImageFavorite.add(rImageFavorite)
                            queryNewText.addValueEventListener(textEventListener)
                        }

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        if(query.database!=null)
            query.addValueEventListener(eventListener)
    }

}

