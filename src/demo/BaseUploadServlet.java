package demo;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BaseUploadServlet extends HttpServlet {

  protected static final int MAX_UPLOAD_FILE_SIZE = 500 * 1000; // 500KB

  private static final int MAX_BLOB_SIZE = 2 * 1000000000; // 2GB

  private static final String PARAMETER_BLOBCOUNT = "blobcount";

  private static final Logger log = Logger.getLogger(BaseUploadServlet.class.getName());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
    BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
    String self = bs.createUploadUrl("/upload");

    resp.getWriter().println("<html><body>");

    printForm(resp, "/file-upload", "Do it yourself processing", MAX_UPLOAD_FILE_SIZE);
    printForm(resp, bs.createUploadUrl("/blob-upload"), "Using blobstore", MAX_BLOB_SIZE);

    String blobcount = req.getParameter(PARAMETER_BLOBCOUNT);
    if (blobcount != null) {
      resp.getWriter().println(
          "<div style='font-weight:bold;color:green;'>Thank you for uploading " + blobcount
              + " blob(s).</div>");
    }

    resp.getWriter().println("</body></html>");
  }

  private void printForm(HttpServletResponse resp, String url, String description, int maxFileSize)
      throws IOException {
    resp.getWriter().println(
        "<fieldset><legend style='font-weight:bold;'>" + description + "</legend>");

    resp.getWriter().println(
        "Maximum file size: " + maxFileSize + " bytes"
            + "<form method='post' enctype='multipart/form-data' action=" + url + ">"
            + "<input type=text name=mytext value='Your comment here'>"
            + "<input type=file name=myfile>" + "<input type=file name=myfile2>"
            + "<input type=submit value='" + url + "'>" + "</form>");

    resp.getWriter().println("</fieldset><br/>");
  }

}
