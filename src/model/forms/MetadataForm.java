package model.forms;

import formsfx.model.structure.Form;
import model.core.Metadata;

public abstract class MetadataForm {

    protected Metadata metadata;
    public Metadata getMetadata() {
        return metadata;
    }

    protected Form formInstance;
    public Form getFormInstance() {
        if (formInstance == null) {
            createForm();
        }
        return formInstance;
    }

    public MetadataForm(Metadata metadata) {
        this.metadata = metadata;
    }

    abstract protected void createForm();

}
