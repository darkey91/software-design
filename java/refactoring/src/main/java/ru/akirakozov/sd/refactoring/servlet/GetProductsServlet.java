package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.entity.Product;
import ru.akirakozov.sd.refactoring.html.HtmlStringBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    private final ProductDao productDao;

    public GetProductsServlet(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final HtmlStringBuilder htmlBuilder = new HtmlStringBuilder();

        try {
            for (final Product product : productDao.findAll()) {
                htmlBuilder.breakLine(product.getName() + "\t" + product.getPrice());
            }
            response.getWriter().println(htmlBuilder.toString());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
