/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.model.file.UploadedFile;

@FacesValidator("fileTypeValidatorv2")
public class FileTypeValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        System.out.println("kvalidator");
        if (value instanceof UploadedFile) {
            UploadedFile file = (UploadedFile) value;
            String fileName = file.getFileName();
            
            if (!fileName.toLowerCase().endsWith(".png")) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid file type. Please upload only .png files.", null);
                throw new ValidatorException(message);
            }
        }
    }
}
