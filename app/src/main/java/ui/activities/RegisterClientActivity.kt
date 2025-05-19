package ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.databinding.ActivityRegisterClientBinding
import android.content.Intent

class RegisterClientActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConsultar.setOnClickListener {
            val dniIngresado = binding.etNumeroDocumento.text.toString()
            val intent = Intent(this, RegistrationActivity::class.java)
            intent.putExtra("dni_ingresado", dniIngresado)
            startActivity(intent) }

        setupCustomSpinner(binding.spinnerTipoDocumento, R.array.tipo_documento_array)
    }

    private fun setupCustomSpinner(spinner: Spinner, arrayResId: Int) {
        val items = resources.getStringArray(arrayResId).toList()

        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item,
            items
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0 // Deshabilita la primera opción como hint
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_text)
                textView.text = getItem(position)

                val colorRes = if (position == 0) R.color.hint_color else android.R.color.black
                textView.setTextColor(ContextCompat.getColor(context, colorRes))

                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_text)
                textView.text = getItem(position)

                val colorRes = if (position == 0) R.color.hint_color else android.R.color.black
                textView.setTextColor(ContextCompat.getColor(context, colorRes))

                return view
            }
        }

        spinner.adapter = adapter
        spinner.background = ContextCompat.getDrawable(this, R.drawable.edit_text_layer)
        spinner.setSelection(0)
    }
}
