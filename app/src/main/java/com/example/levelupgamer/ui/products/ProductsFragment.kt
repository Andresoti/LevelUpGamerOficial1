package com.example.levelupgamer.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.levelupgamer.adapters.ProductAdapter
import com.example.levelupgamer.api.ProductRepository
import com.example.levelupgamer.databinding.FragmentProductsBinding
import com.example.levelupgamer.models.GamingProduct
import kotlinx.coroutines.launch

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private val repository = ProductRepository()
    private val products = mutableListOf<GamingProduct>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        loadProducts()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            products = products,
            onEditClick = { product -> showEditDialog(product) },
            onDeleteClick = { product -> confirmDelete(product) }
        )

        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }
    }

    private fun setupListeners() {
        binding.fabAddProduct.setOnClickListener {
            showAddDialog()
        }

        binding.swipeRefresh.setOnRefreshListener {
            loadProducts()
        }
    }

    private fun loadProducts() {
        binding.swipeRefresh.isRefreshing = true

        lifecycleScope.launch {
            try {
                products.clear()
                products.addAll(getSampleProducts())
                productAdapter.notifyDataSetChanged()

                binding.swipeRefresh.isRefreshing = false

                if (products.isEmpty()) {
                    binding.tvEmptyState.visibility = View.VISIBLE
                    binding.recyclerViewProducts.visibility = View.GONE
                } else {
                    binding.tvEmptyState.visibility = View.GONE
                    binding.recyclerViewProducts.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                binding.swipeRefresh.isRefreshing = false
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(
            com.example.levelupgamer.R.layout.dialog_product,
            null
        )

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Agregar Producto Gaming")
            .setView(dialogView)
            .setPositiveButton("Guardar") { dialog, _ ->
                val product = extractProductFromDialog(dialogView)
                if (validateProduct(product)) {
                    createProduct(product)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun showEditDialog(product: GamingProduct) {
        val dialogView = layoutInflater.inflate(
            com.example.levelupgamer.R.layout.dialog_product,
            null
        )

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Editar Producto")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { dialog, _ ->
                val updatedProduct = extractProductFromDialog(dialogView)
                if (validateProduct(updatedProduct)) {
                    updateProduct(product.id, updatedProduct)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun extractProductFromDialog(view: View): GamingProduct {
        return GamingProduct(
            name = "Producto Nuevo",
            category = "Mouse",
            brand = "Logitech",
            price = 99990.0,
            description = "Producto gaming de alta calidad",
            stock = 10,
            specifications = "RGB, Alta precisión"
        )
    }

    private fun validateProduct(product: GamingProduct): Boolean {
        if (product.name.isEmpty()) {
            Toast.makeText(requireContext(), "El nombre es requerido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (product.price <= 0) {
            Toast.makeText(requireContext(), "El precio debe ser mayor a 0", Toast.LENGTH_SHORT).show()
            return false
        }

        if (product.stock < 0) {
            Toast.makeText(requireContext(), "El stock no puede ser negativo", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun createProduct(product: GamingProduct) {
        lifecycleScope.launch {
            try {
                products.add(product.copy(id = products.size + 1))
                productAdapter.notifyItemInserted(products.size - 1)

                Toast.makeText(
                    requireContext(),
                    "✓ Producto agregado: ${product.name}",
                    Toast.LENGTH_SHORT
                ).show()

                binding.tvEmptyState.visibility = View.GONE
                binding.recyclerViewProducts.visibility = View.VISIBLE
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateProduct(id: Int, product: GamingProduct) {
        lifecycleScope.launch {
            try {
                val index = products.indexOfFirst { it.id == id }
                if (index != -1) {
                    products[index] = product.copy(id = id)
                    productAdapter.notifyItemChanged(index)

                    Toast.makeText(
                        requireContext(),
                        "✓ Producto actualizado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun confirmDelete(product: GamingProduct) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Producto")
            .setMessage("¿Estás seguro de eliminar ${product.name}?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteProduct(product)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteProduct(product: GamingProduct) {
        lifecycleScope.launch {
            try {
                val index = products.indexOf(product)
                if (index != -1) {
                    products.removeAt(index)
                    productAdapter.notifyItemRemoved(index)

                    Toast.makeText(
                        requireContext(),
                        "✓ Producto eliminado",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (products.isEmpty()) {
                        binding.tvEmptyState.visibility = View.VISIBLE
                        binding.recyclerViewProducts.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getSampleProducts(): List<GamingProduct> {
        return listOf(
            GamingProduct(
                id = 1,
                name = "Logitech G Pro X Superlight",
                category = "Mouse",
                brand = "Logitech",
                price = 149990.0,
                description = "Mouse gaming inalámbrico ultra ligero",
                stock = 15,
                specifications = "25K DPI, RGB, <63g"
            ),
            GamingProduct(
                id = 2,
                name = "Corsair K70 RGB",
                category = "Teclado",
                brand = "Corsair",
                price = 189990.0,
                description = "Teclado mecánico RGB",
                stock = 8,
                specifications = "Cherry MX Red, RGB, Aluminio"
            ),
            GamingProduct(
                id = 3,
                name = "HyperX Cloud II",
                category = "Audífonos",
                brand = "HyperX",
                price = 89990.0,
                description = "Audífonos gaming 7.1",
                stock = 20,
                specifications = "7.1 Surround, 53mm drivers"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}