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
import com.example.levelupgamer.models.Validator
import com.google.android.material.textfield.TextInputEditText
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
                // 🔥 LLAMADA REAL A LA API
                val result = repository.getAllProducts()

                result.onSuccess { productList ->
                    products.clear()
                    products.addAll(productList)
                    productAdapter.notifyDataSetChanged()

                    updateEmptyState()

                    Toast.makeText(
                        requireContext(),
                        "✓ ${products.size} productos cargados desde API",
                        Toast.LENGTH_SHORT
                    ).show()
                }.onFailure { error ->
                    Toast.makeText(
                        requireContext(),
                        "Error API: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                binding.swipeRefresh.isRefreshing = false
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
                if (product != null && validateProduct(product)) {
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

        // 🔥 RELLENAR CAMPOS CON DATOS EXISTENTES
        dialogView.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductName)
            ?.setText(product.name)
        dialogView.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductCategory)
            ?.setText(product.category)
        dialogView.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductBrand)
            ?.setText(product.brand)
        dialogView.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductPrice)
            ?.setText(product.price.toString())
        dialogView.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductStock)
            ?.setText(product.stock.toString())
        dialogView.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductDescription)
            ?.setText(product.description)
        dialogView.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductSpecs)
            ?.setText(product.specifications)

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Editar Producto")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { dialog, _ ->
                val updatedProduct = extractProductFromDialog(dialogView)
                if (updatedProduct != null && validateProduct(updatedProduct)) {
                    updateProduct(product.id, updatedProduct)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    // 🔥 FUNCIÓN CORREGIDA - EXTRAE DATOS REALES DEL DIÁLOGO
    private fun extractProductFromDialog(view: View): GamingProduct? {
        try {
            val name = view.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductName)
                ?.text.toString().trim()
            val category = view.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductCategory)
                ?.text.toString().trim()
            val brand = view.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductBrand)
                ?.text.toString().trim()
            val priceStr = view.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductPrice)
                ?.text.toString().trim()
            val stockStr = view.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductStock)
                ?.text.toString().trim()
            val description = view.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductDescription)
                ?.text.toString().trim()
            val specs = view.findViewById<TextInputEditText>(com.example.levelupgamer.R.id.etProductSpecs)
                ?.text.toString().trim()

            val price = priceStr.toDoubleOrNull() ?: 0.0
            val stock = stockStr.toIntOrNull() ?: 0

            return GamingProduct(
                name = name,
                category = category,
                brand = brand,
                price = price,
                description = description,
                stock = stock,
                specifications = specs
            )
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al leer datos: ${e.message}", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    private fun validateProduct(product: GamingProduct): Boolean {
        if (product.name.isEmpty()) {
            Toast.makeText(requireContext(), "❌ El nombre es requerido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (product.category.isEmpty()) {
            Toast.makeText(requireContext(), "❌ La categoría es requerida", Toast.LENGTH_SHORT).show()
            return false
        }

        if (product.brand.isEmpty()) {
            Toast.makeText(requireContext(), "❌ La marca es requerida", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Validator.isValidPrice(product.price)) {
            Toast.makeText(requireContext(), "❌ El precio debe ser mayor a 0", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Validator.isValidStock(product.stock)) {
            Toast.makeText(requireContext(), "❌ El stock no puede ser negativo", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun createProduct(product: GamingProduct) {
        lifecycleScope.launch {
            try {
                // 🔥 LLAMADA REAL A LA API
                val result = repository.createProduct(product)

                result.onSuccess { createdProduct ->
                    products.add(createdProduct)
                    productAdapter.notifyItemInserted(products.size - 1)

                    Toast.makeText(
                        requireContext(),
                        "✓ Producto creado en API: ${createdProduct.name}",
                        Toast.LENGTH_SHORT
                    ).show()

                    updateEmptyState()
                }.onFailure { error ->
                    Toast.makeText(
                        requireContext(),
                        "Error al crear: ${error.message}",
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

    private fun updateProduct(id: Int, product: GamingProduct) {
        lifecycleScope.launch {
            try {
                // 🔥 LLAMADA REAL A LA API
                val result = repository.updateProduct(id, product)

                result.onSuccess { updatedProduct ->
                    val index = products.indexOfFirst { it.id == id }
                    if (index != -1) {
                        products[index] = updatedProduct
                        productAdapter.notifyItemChanged(index)

                        Toast.makeText(
                            requireContext(),
                            "✓ Producto actualizado en API",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.onFailure { error ->
                    Toast.makeText(
                        requireContext(),
                        "Error al actualizar: ${error.message}",
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
                // 🔥 LLAMADA REAL A LA API
                val result = repository.deleteProduct(product.id)

                result.onSuccess {
                    val index = products.indexOf(product)
                    if (index != -1) {
                        products.removeAt(index)
                        productAdapter.notifyItemRemoved(index)

                        Toast.makeText(
                            requireContext(),
                            "✓ Producto eliminado de API",
                            Toast.LENGTH_SHORT
                        ).show()

                        updateEmptyState()
                    }
                }.onFailure { error ->
                    Toast.makeText(
                        requireContext(),
                        "Error al eliminar: ${error.message}",
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

    private fun updateEmptyState() {
        if (products.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.recyclerViewProducts.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.recyclerViewProducts.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}