package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.enums.QueryCommand;
import ru.akirakozov.sd.refactoring.html.HtmlStringBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final ProductDao productDao;

    public QueryServlet(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        final String commandStr = request.getParameter("command");
        String responseContent = null;

        final HtmlStringBuilder htmlBuilder = new HtmlStringBuilder();
        final QueryCommand command = QueryCommand.isAllowedCommand(commandStr)
                ? QueryCommand.valueOf(commandStr.toUpperCase())
                : QueryCommand.UNKNOWN;

        switch (command) {
            case MAX: {
                htmlBuilder.firstHeader("Product with max price: ");
                Optional.ofNullable(productDao.maxByPrice())
                        .ifPresent(product ->
                                htmlBuilder.breakLine(product.getName() + "\t" + product.getPrice()));
                break;
            }
            case MIN: {
                htmlBuilder.firstHeader("Product with min price: ");
                Optional.ofNullable(productDao.minByPrice())
                        .ifPresent(product ->
                                htmlBuilder.breakLine(product.getName() + "\t" + product.getPrice()));
                break;
            }
            case SUM: {
                final long sum = productDao.sum();
                htmlBuilder
                        .line("Summary price: ")
                        .line(Long.toString(sum));
                break;
            }
            case COUNT: {
                final long totalCount = productDao.countAll();
                htmlBuilder
                        .line("Number of products: ")
                        .line(Long.toString(totalCount));
                break;
            }
            default:
                responseContent = "Unknown command: " + commandStr;
        }

        if (responseContent == null)
            responseContent = htmlBuilder.toString();


        try {
            response.getWriter().println(responseContent);
        } catch (final Exception exception) {
            throw new RuntimeException();
        }
    }
}
