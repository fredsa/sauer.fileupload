package demo;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BlobUploadServlet extends BaseUploadServlet {

  private static final String PARAMETER_BLOBCOUNT = "blobcount";

  private static final Logger log = Logger.getLogger(BlobUploadServlet.class.getName());

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    try {
      log.info("Trying blobstore service...");
      BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
      Map<String, BlobKey> blobs = bs.getUploadedBlobs(req);
      if (!blobs.isEmpty()) {
        BlobKey blobKey = blobs.get("myFile");
        for (Entry<String, BlobKey> entry : blobs.entrySet()) {
          log.info("Got blob: " + entry.getKey() + ", " + entry.getValue());
          BlobInfo info = new BlobInfoFactory().loadBlobInfo(entry.getValue());
          log.info("- content type: " + info.getContentType());
          log.info("- filename: " + info.getFilename());
          log.info("- size: " + info.getSize());
          log.info("- blobkey: " + info.getBlobKey());
          log.info("- creation: " + info.getCreation());
        }

      }
      resp.sendRedirect(req.getRequestURI() + "?" + PARAMETER_BLOBCOUNT + "=" + blobs.size());
      return;

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new ServletException(ex);
    }
  }
}
