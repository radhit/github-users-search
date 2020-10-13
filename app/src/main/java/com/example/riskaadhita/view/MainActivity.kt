package com.example.riskaadhita.view

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import com.example.riskaadhita.R
import com.example.riskaadhita.adapter.UsersAdapter
import com.example.riskaadhita.api.DataResponse
import com.example.riskaadhita.base.BaseActivity
import com.example.riskaadhita.data.Item
import com.example.riskaadhita.helper.EndlessScrollListener
import com.example.riskaadhita.helper.RecyclerViewClickListener
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : BaseActivity() {
    override fun getView(): Int = R.layout.activity_main
    lateinit var adapter: UsersAdapter
    var listUsers: ArrayList<Item> = arrayListOf()
    var page = 1
    var delay: Long = 1000
    var lastEdit: Long = 0
    var handler: Handler = Handler()

    private val finishChecker = Runnable {
        if (System.currentTimeMillis() > lastEdit + delay - 500) {
            if (!et_search.text.toString().equals("")){
                page = 1
                getData()
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!p0.toString().equals("")){
                    println("masuk after text")
                    lastEdit = System.currentTimeMillis()
                    handler.postDelayed(finishChecker, delay)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().equals("")) {
                    println("masuk on text")
                    handler.removeCallbacks(finishChecker)
                }
            }
        })
        et_search.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (et_search.text.toString().equals("")){
                    Toast.makeText(this@MainActivity, "Fill search box first!", Toast.LENGTH_LONG).show()
                } else{
                    page = 1
                    getData()
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getData(keterangan: String = "") {
        closeKeyboard(ll_search)
        tv_loading.visibility = View.VISIBLE
        tv_notfound.visibility = View.GONE
        var url = ""
        url = "/search/users?q=${et_search.text}&page=${page}&per_page=20"

        val call: Call<DataResponse<List<Item>>> = baseApi.getUsers(url)
        call.enqueue(object : Callback<DataResponse<List<Item>>> {
            override fun onResponse(call: Call<DataResponse<List<Item>>>?, response: Response<DataResponse<List<Item>>>?) {
                tv_loading.visibility = View.GONE
                if (response!!.isSuccessful) {
                    listUsers.clear()
                    listUsers.addAll(response.body()!!.items!!)
                    if (listUsers.size > 0) {
                        if (keterangan.equals("load")) {
                            for (data in listUsers) {
                                adapter.lists.add(data)
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter.lists.clear()
                            adapter.lists.addAll(listUsers)
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        adapter.lists.clear()
                        adapter.notifyDataSetChanged()
                        tv_notfound.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity, "User not found", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<List<Item>>>?, t: Throwable?) {
                tv_loading.visibility = View.GONE
                Toast.makeText(this@MainActivity, t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setRecycleView(){
        adapter = UsersAdapter(this)
        rv_list.adapter = adapter
        initiateLinearLayoutRecyclerView(rv_list, object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {

            }

            override fun onLongClick(view: View, position: Int) {
            }
        })
        rv_list.addOnScrollListener(
            object : EndlessScrollListener() {
                override fun onLoadMore() {
                    page += 1
                    if (listUsers.size > 19) {
                        println("addOnScrollListener nextPage")
                        getData("load")
                    }
                }

            }
        )
    }

    private fun closeKeyboard(ll_search: LinearLayout) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(ll_search.windowToken, 0)
    }
}