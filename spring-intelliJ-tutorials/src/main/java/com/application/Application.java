package com.application;

import com.application.entities.*;
import com.application.interfaces.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

class ApiKey {
	private static final String API_KEY = "YOUR_API_KEY";

	public static String getApiKey() {
		return API_KEY;
	}
}


// Đăng nhập:
// POST
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class UserController {
	@PersistenceContext
	private EntityManager entityManager;

	@PostMapping("/login")
	public String login(@RequestBody User user, @RequestHeader(value="X-API-KEY") String apiKey) {
		if (!"YOUR_API_KEY".equals(apiKey)) {
			return "Invalid API Key";
		}

		String sqlString = "SELECT * FROM Users WHERE UserName = :userName AND Password = :password";
		Query query = entityManager.createNativeQuery(sqlString, User.class);
		query.setParameter("userName", user.getUserName());
		query.setParameter("password", user.getPassword());
		List<User> users = query.getResultList();

		if (!users.isEmpty()) {
			return "Login successful for user: " + user.getUserName();
		} else {
			return "Invalid username or password";
		}
	}
}

// Nhà cung cấp - Supplier
// POST, GET. PUT, DEL
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class SupplierController {

	@Autowired
	private SupplierRepository supplierRepository;

	@PostMapping("/addSupplier")
	public Supplier addSupplier(@RequestBody Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	@GetMapping("/getSuppliers")
	public List<Supplier> getSuppliers() {
		return supplierRepository.findAll();
	}

	@PutMapping("/putSupplier")
	public Supplier putSupplier(@RequestBody Supplier supplier) {
		if (supplierRepository.existsById((long) supplier.getId())) {
			supplierRepository.save(supplier);
		}
		return supplierRepository.save(supplier);
	}

	@DeleteMapping("/delSupplier/{id}")
	public String delSupplier(@PathVariable Long id) {
		if (supplierRepository.existsById(id)) {
			supplierRepository.deleteById(id);
			return "Delete supplier successfully: " + id;
		} else {
			return "Supplier not found: " + id;
		}
	}
}

// Khách hàng - Customer
// POST, GET. PUT, DEL
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;

	@PostMapping("/addCustomer")
	public Customer addCustomer(@RequestBody Customer customer) {
		return customerRepository.save(customer);
	}

	@GetMapping("/getCustomers")
	public List<Customer> getCustomers() {
		return customerRepository.findAll();
	}

	@PutMapping("/putCustomer")
	public Customer putCustomer(@RequestBody Customer customer) {
		if (customerRepository.existsById((long) customer.getId())) {
			customerRepository.save(customer);
		}
		return customerRepository.save(customer);
	}

	@DeleteMapping("/delCustomer/{id}")
	public String delCustomer(@PathVariable Long id) {
		if (customerRepository.existsById(id)) {
			customerRepository.deleteById(id);
			return "Delete customer successfully: " + id;
		} else {
			return "Customer not found: " + id;
		}
	}
}

// Hàng hóa - Product
// POST, GET, PUT
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class ProductController {
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/getProducts")
	public List<Product> getProducts() {
		return productRepository.findAll();
	}

	@GetMapping("/searchProducts")
	public List<Product> searchProducts() {
		return productRepository.findByInventoryGreaterThan(0);
	}

	@GetMapping("/searchProducts/{displayName}")
	public List<Product> searchProductsByName(@PathVariable String displayName) {
		return productRepository.findAllByDisplayNameContainingAndInventoryGreaterThan(displayName, 0);
	}

	@PostMapping("/addProduct")
	public Product addProduct(@RequestBody Product product) {
		return productRepository.save(product);
	}

	@PutMapping("/putProduct")
	public Product putProduct(@RequestBody Product product) {
		if (productRepository.existsById((long) product.getId())) {
			productRepository.save(product);
		}
		return productRepository.save(product);
	}
}

// Phiếu nhập - Receipt
// Receipt: POST
// ReceiptRow: POST
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class ReceiptController {
	@Autowired
	private ReceiptRepository receiptRepository;

	@Autowired
	private ReceiptRowRepository receiptRowRepository;

	@Autowired
	private EntityManager entityManager;

	@PostMapping("/addReceipt")
	public Receipt addReceipt(@RequestBody Receipt receipt) {
		// Set the receipt date to the current date
		receipt.setReceiptDate(new Date());

		// Save the receipt
		return receiptRepository.save(receipt);
	}

	@GetMapping("/getReceipts")
	public List<Receipt> getExports() {
		return receiptRepository.findAll();
	}

	@PostMapping("/addReceiptRow")
	public ReceiptRow addReceiptRow(@RequestBody ReceiptRow receiptRow) {
		return receiptRowRepository.save(receiptRow);
	}

	@GetMapping("/getReceiptRows/{idReceipt}")
	public List<Map<String, Object>> getReceiptRows(@PathVariable Integer idReceipt) {
		Query query = entityManager.createQuery(
				"SELECT rr.id, p.displayName, s.displayName, rr.quantity, rr.receiptPrice, rr.totalPrice " +
						"FROM ReceiptRow rr " +
						"INNER JOIN Product p ON rr.idProduct = p.id " +
						"INNER JOIN Supplier s ON rr.idSupplier = s.id " +
						"WHERE rr.idReceipt = :idReceipt"
		);
		query.setParameter("idReceipt", idReceipt);

		List<Object[]> results = query.getResultList();
		List<Map<String, Object>> resultList = new ArrayList<>();

		for (Object[] result : results) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("id", result[0]);
			resultMap.put("productName", result[1]);
			resultMap.put("supplierName", result[2]);
			resultMap.put("quantity", result[3]);
			resultMap.put("receiptPrice", result[4]);
			resultMap.put("totalPrice", result[5]);
			resultList.add(resultMap);
		}
		return resultList;
	}
}

// Phiếu xuất - Export
// Export: POST
// ExportRow: POST
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class ExportController {
	@Autowired
	private ExportRepository exportRepository;

	@Autowired
	private ExportRowRepository exportRowRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private EntityManager entityManager;

	@PostMapping("/addExport")
	public Export addExport(@RequestBody Export export) {
		// Set the receipt date to the current date
		export.setExportDate(new Date());

		// Save the receipt
		return exportRepository.save(export);
	}

	@GetMapping("/getExports")
	public List<Export> getExports() {
		return exportRepository.findAll();
	}

	@PostMapping("/addExportRow")
	public ResponseEntity<?> addExportRow(@RequestBody ExportRow exportRow) {
		Product product = productRepository.findById((long) exportRow.getIdProduct()).orElse(null);

		if (product != null) {
			if (exportRow.getQuantity() <= product.getInventory()) {
				return new ResponseEntity<>(exportRowRepository.save(exportRow), HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Số lượng vượt quá cho phép", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deleteExport/{id}")
	public String delExport(@PathVariable Long id) {
		if (exportRepository.existsById(id)) {
			exportRepository.deleteById(id);
			return "Delete Export successfully: " + id;
		} else {
			return "Export not found: " + id;
		}
	}

	@GetMapping("/getExportRows/{idExport}")
	public List<Map<String, Object>> getExportRows(@PathVariable Integer idExport) {
		Query query = entityManager.createQuery(
				"SELECT er.id, p.displayName, c.displayName, er.quantity, er.exportPrice, er.totalPrice " +
						"FROM ExportRow er " +
						"INNER JOIN Product p ON er.idProduct = p.id " +
						"INNER JOIN Customer c ON er.idCustomer = c.id " +
						"WHERE er.idExport = :idExport"
		);
		query.setParameter("idExport", idExport);

		List<Object[]> results = query.getResultList();
		List<Map<String, Object>> resultList = new ArrayList<>();

		for (Object[] result : results) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("id", result[0]);
			resultMap.put("productName", result[1]);
			resultMap.put("customerName", result[2]);
			resultMap.put("quantity", result[3]);
			resultMap.put("exportPrice", result[4]);
			resultMap.put("totalPrice", result[5]);
			resultList.add(resultMap);
		}
		return resultList;
	}
}