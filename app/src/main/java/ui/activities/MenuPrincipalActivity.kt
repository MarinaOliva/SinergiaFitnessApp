package ui.activities

import android.os.Bundle
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.databinding.ActivityMenuPrincipalBinding
import android.content.Intent

class MenuPrincipalActivity : BaseActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el contenido y agregarlo al contentFrame definido en BaseActivity
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botones
        binding.btnBuscarRegistrar.setOnClickListener {
            val intent = Intent(this, RegisterClientActivity::class.java)
            startActivity(intent)
        }

        binding.btnAbonarCuota.setOnClickListener {
            val intent = Intent(this, PayFeeActivity::class.java)
            startActivity(intent)
        }

        binding.btnActividadesGuiadas.setOnClickListener {
            val intent = Intent(this, EnrollActivitiesActivity::class.java)
            startActivity(intent)
        }

        binding.btnCuotasVencidas.setOnClickListener {
            val intent = Intent(this, CheckOverdueFeesActivity::class.java)
            startActivity(intent)
        }

        binding.btnCerrarSesion.setOnClickListener {
            // Volver al login y cerrar sesión
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Cierra la actividad actual
        }
    }
}
