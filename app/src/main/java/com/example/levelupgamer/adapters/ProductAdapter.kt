// RUTA: app/src/main/java/com/example/levelupgamer/adapters/ProductAdapter.kt
// NOMBRE DEL ARCHIVO: ProductAdapter.kt
// CREAR CARPETA: adapters

package com.example.levelupgamer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.levelupgamer.databinding.ItemProductBinding
import com.example.levelupgamer.models.GamingProduct
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val products: List<GamingProduct>,
    private val onEditClick: (GamingProduct) -> Unit,
    private val onDeleteClick: (GamingProduct) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: GamingProduct) {
            binding.apply {
                tvProductName.text = product.name
                tvProductBrand.text = product.brand
                tvProductCategory.text = product.category
                tvProductPrice.text = formatPrice(product.price)
                tvProductStock.text = "Stock: ${product.stock}"
                tvProductDescription.text = product.description
                tvProductSpecs.text = product.specifications

                if (product.stock <= 5) {
                    tvProductStock.setTextColor(
                        root.context.getColor(android.R.color.holo_red_dark)
                    )
                } else if (product.stock <= 10) {
                    tvProductStock.setTextColor(
                        root.context.getColor(android.R.color.holo_orange_dark)
                    )
                } else {
                    tvProductStock.setTextColor(
                        root.context.getColor(android.R.color.holo_green_dark)
                    )
                }

                btnEdit.setOnClickListener {
                    onEditClick(product)
                }

                btnDelete.setOnClickListener {
                    onDeleteClick(product)
                }
            }
        }

        private fun formatPrice(price: Double): String {
            val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            return formatter.format(price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}