package com.trungtamjava.controller.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trungtamjava.dao.ProductDao;
import com.trungtamjava.dao.impl.ProductDaoImpl;
import com.trungtamjava.model.BillProduct;
import com.trungtamjava.model.Product;

@WebServlet(urlPatterns = "/add-to-cart")
public class AddToCartController extends HttpServlet {
	ProductDao productDao = new ProductDaoImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pId = req.getParameter("pid");
		Product product = productDao.getId(Integer.parseInt(pId));

		HttpSession session = req.getSession();
		Object obj = session.getAttribute("cart");
		if (obj == null) {

			BillProduct billProduct = new BillProduct();
			billProduct.setProduct(product);
			billProduct.setQuantity(1);
			billProduct.setUnitPrice(product.getPrice());

			Map<String, BillProduct> map = new HashMap<>();
			map.put(pId, billProduct);

			session.setAttribute("cart", map);
		} else {
			Map<String, BillProduct> map = (Map<String, BillProduct>) obj;

			BillProduct billProduct = map.get(pId);

			if (billProduct == null) {
				billProduct = new BillProduct();
				billProduct.setProduct(product);
				billProduct.setQuantity(1);
				billProduct.setUnitPrice(product.getPrice());

				map.put(pId, billProduct);
			} else {

				billProduct.setQuantity(billProduct.getQuantity() + 1);
			}

			session.setAttribute("cart", map);
		}

		resp.sendRedirect(req.getContextPath() + "/cart");
	}
}
