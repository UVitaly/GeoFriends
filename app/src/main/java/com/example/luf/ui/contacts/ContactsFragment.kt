package com.example.luf.ui.contacts

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.luf.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.*


class ContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel
    var mAuth = FirebaseAuth.getInstance()
    var db : DatabaseReference? = FirebaseDatabase.getInstance().reference
    var doc: Document? = null
  //  var simpleSearchView: SearchView = requireActivity().findViewById(R.id.search_view) as SearchView // inititate a search view


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        contactsViewModel =
            ViewModelProvider(this).get(ContactsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contacts, container, false)
        return root
    }


    override fun onResume()
    {
        super.onResume()

        var simpleSearchView:SearchView = requireActivity().findViewById(R.id.search_view)

        simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                val rootRef = FirebaseDatabase.getInstance().reference
                val usersRef = rootRef.child("User")
                val eventListener: ValueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            if (ds.child("fullName").value == query ) {

                                val cv: CardView = CardView(requireContext())
                                val tv: TextView = TextView(requireContext())
                                val btn: ImageButton = ImageButton(requireContext())

                                var paramsImage: FrameLayout.LayoutParams = FrameLayout.LayoutParams(100, 100)
                                var paramsText: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                                var paramsButton: FrameLayout.LayoutParams = FrameLayout.LayoutParams(100, 100)

                                val act = this@ContactsFragment.requireActivity()
                                val linearLayout: LinearLayout = act.findViewById(R.id.scrollLayout_contacts)

                                paramsButton.gravity = Gravity.RIGHT
                                btn.setImageResource(R.drawable.ic_menu_send)
                                btn.setOnClickListener(){
                                    var tempds : DatabaseReference? = FirebaseDatabase.getInstance().getReference("Contacts")
                                    var key = db!!.push().key.toString()
                                    var dsChildren = tempds!!.child(key)
                                    db!!.ref.child("Contacts").child(key)
                                    db!!.ref.child("Contacts").child(key).child("idFirstClient").setValue(ds.key)
                                    db!!.ref.child("Contacts").child(key).child("idSecondClient").setValue(FirebaseAuth.getInstance().currentUser!!.uid)
                                    db!!.ref.child("Contacts").child(key).child("isFavorite").setValue(0)
                                    db!!.ref.child("Contacts").child(key).child("locationPermission").setValue(0)
                                }
                                tv.text = ds.child("fullName").value as CharSequence
                                tv.setTextColor(Color.GRAY)
                                tv.gravity = Gravity.CENTER
                                tv.textSize = 22f
                                tv.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_black)


                                val rImageView: CircleImageView = CircleImageView(requireContext())
                                rImageView.setImageResource(R.drawable.ic_menu_camera)
                                paramsImage.gravity = Gravity.LEFT

                                cv.addView(rImageView, paramsImage)
                                cv.addView(tv, paramsText)
                                cv.addView(btn, paramsButton)
                                linearLayout.addView(cv)
                                }

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                }
                usersRef.addListenerForSingleValueEvent(eventListener)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }


        })

        }

}


