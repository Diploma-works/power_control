package ru.foody.fragments.products

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import ru.foody.MainActivity
import ru.foody.R
import ru.guybydefault.foody.domain.Product
import java.time.Instant
import java.util.*

interface ProductAmountDialogListener {
    fun onDialogPositiveClick(dialog: DialogFragment, product: Product, amount: Double)
    fun onDialogNegativeClick(dialog: DialogFragment, product: Product)
}

class SearchProductFragment : Fragment(),
    ProductAmountDialogListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ProductToViewHolderAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var fullList: MutableList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_search_products, container, false)

        recyclerView = view.findViewById(R.id.searched_products_recyclerview)
        recyclerView.isNestedScrollingEnabled = false;

        fullList = mutableListOf<Product>()
        searchProducts("")
        viewAdapter = ProductToViewHolderAdapter(fullList, this)

        viewManager = LinearLayoutManager(context)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        view.findViewById<EditText>(R.id.search_edit_text).addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                private var timer: Timer = Timer()
                private val DELAY: Long = 500 // Milliseconds

                override fun afterTextChanged(editable: Editable) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            if (isAdded) { // if fragment is still attached to activity and is actual
                                searchProducts(editable.toString())
                            }
                        }
                    }, DELAY)
                }
            })
        return view
    }

    private fun searchProducts(searchString: String) {
        (requireActivity() as MainActivity).diaryService.getProducts(searchString) { list: MutableList<Product> ->
            run {
                this.viewAdapter.productsList = list
                viewAdapter.notifyDataSetChanged()
            }
        }
//        viewAdapter.productsList =
//            fullList.filter { product: Product -> product.name.contains(searchString) }
    }

//    private fun initProducts(): MutableList<Product> {
//        val list = mutableListOf<Product>()
//        val count = 10
//        val nutrientsList = DummyObjectsGenerationUtility.genNutrientsInfo(count)
//        for (i in 0..count) {
//            list.add(Product("Product $i", nutrientsList.get(i), Instant.now(), Instant.now()))
//        }
//        (requireActivity() as MainActivity).diaryService.
//
//        return list
//    }

    override fun onDialogPositiveClick(dialog: DialogFragment, product: Product, amount: Double) {
        (requireActivity() as MainActivity).diaryService.addDiaryPosition(
            product.name,
            Instant.now(),
            product,
            amount
        )
    }

    override fun onDialogNegativeClick(dialog: DialogFragment, product: Product) {

    }

    class ProductAmountDialog(val product: Product) : DialogFragment() {
        lateinit var listener: ProductAmountDialogListener

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val layoutInflater = requireActivity().layoutInflater
                val view = layoutInflater.inflate(R.layout.add_product_to_diary_dialog, null)
                val amountField = view.findViewById<TextInputEditText>(R.id.productAmount)
                builder.setView(view)
                    .setPositiveButton("add") { dialog, which ->
                        //TODO обработка невведенных или невалидных значений
                        val amount = amountField.text.toString().toDoubleOrNull()
                        if (amount != null) {
                            listener.onDialogPositiveClick(this, product, amount)
                        }
                    }
                    .setNegativeButton("cancel") { dialog, _ ->
                        listener.onDialogNegativeClick(this, product)
                    }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the NoticeDialogListener so we can send events to the host
                listener = parentFragment as ProductAmountDialogListener
            } catch (e: ClassCastException) {
                // The activity doesn't implement the interface, throw exception
                throw ClassCastException(
                    (context.toString() +
                            " must implement NoticeDialogListener")
                )
            }
        }

        /** The system calls this to get the DialogFragment's layout, regardless
        of whether it's being displayed as a dialog or an embedded fragment. */
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            // Inflate the layout to use as dialog or embedded fragment
            return inflater.inflate(R.layout.add_product_to_diary_dialog, container, false)
        }
    }

}