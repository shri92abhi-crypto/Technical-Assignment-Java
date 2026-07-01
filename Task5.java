import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class DocumentValidator {

    private static final Logger logger = LoggerFactory.getLogger(DocumentValidator.class);

    public ValidationResult validate(Document doc) {
        try {
            if (doc == null) {
                // FIX: Validation failure is an expected business scenario.
                throw new IllegalArgumentException("Document is null");
            }

            String content = doc.extractContent();

            if (content == null || content.isEmpty()) {
                // FIX: Use IllegalArgumentException for expected validation failures.
                throw new IllegalArgumentException("Empty content");
            }

            return runValidationRules(content);

        } catch (IllegalArgumentException e) {
            // FIX: Replace printStackTrace() with logger.warn() for expected
            // validation failures to avoid flooding logs with stack traces.
            logger.warn("Validation failed: {}", e.getMessage());

            // FIX: Return a validation failure result instead of null to prevent
            // NullPointerException in the caller.
            return ValidationResult.invalid(e.getMessage());

        } catch (Exception e) {
            // FIX: Log unexpected runtime exceptions with stack trace.
            logger.error("Unexpected error while validating document.", e);

            return ValidationResult.invalid("Unexpected validation error");
        }
    }

    public void validateBatch(List<Document> docs) {

        for (Document doc : docs) {

            try {
                ValidationResult r = validate(doc);

                // FIX: Check for null before calling isValid() to avoid
                // NullPointerException if validation result is unavailable.
                if (r != null && r.isValid()) {
                    saveResult(r);
                }

            } catch (Exception e) {
                // FIX: Do not swallow exceptions silently. Log them for
                // troubleshooting while allowing batch processing to continue.
                logger.error("Error while processing document in batch.", e);
            }
        }
    }
}