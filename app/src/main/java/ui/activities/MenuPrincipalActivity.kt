package ui.activities

import android.os.Bundle
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.databinding.ActivityMenuPrincipalBinding

class MenuPrincipalActivity : BaseActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el contenido y agregarlo al contentFrame definido en BaseActivity
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botones
        binding.btnBuscarRegistrar.setOnClickListener {
            // Navegar a otra actividad
        }

        binding.btnAbonarCuota.setOnClickListener {
            // Navegar a otra actividad
        }

        binding.btnActividadesGuiadas.setOnClickListener {
            // Navegar a otra actividad
        }

        binding.btnCuotasVencidas.setOnClickListener {
            // Navegar a otra actividad
        }

        binding.btnCerrarSesion.setOnClickListener {
            // Volver al login y cerrar sesión
        }
    }
}
