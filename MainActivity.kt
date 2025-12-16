class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val productList = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(productList) { product ->
            Toast.makeText(this, "Selected: ${product.name}, $${product.price}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("Products")
        fetchProducts()
    }

    private fun fetchProducts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (productSnap in snapshot.children) {
                    val product = productSnap.getValue(Product::class.java)
                    product?.let { productList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load data.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
