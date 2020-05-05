package model.test;

import model.controller.MetadataController;
import model.core.attribute.SimpleAttribute;
import model.core.factory.MetadataFactory;
import model.core.model.Model;
import model.core.model.NumberModel;
import model.core.model.SimpleModel;

public class Test {

    public void initModel() {
        MetadataFactory factory = MetadataFactory.getInstance();

        SimpleModel ingredientModel = (SimpleModel)factory.createNewSimpleModel("Ингредиенты");
        SimpleModel measureModel = (SimpleModel)factory.createNewSimpleModel("ЕдиницыИзмерения");
        SimpleModel recipeLine = (SimpleModel)factory.createNewSimpleModel("СтрокаРецепта");

        factory.createNewSimpleAttribute(recipeLine, "Ингредиент", ingredientModel);
        factory.createNewSimpleAttribute(recipeLine, "ЕдиницаИзмерения", measureModel);
        factory.createNewSimpleAttribute(recipeLine, "Количество", factory.getPredefinedModel(NumberModel.NAME));

        System.out.println(factory.getRootNode().printMetadata(0));
    }

}
