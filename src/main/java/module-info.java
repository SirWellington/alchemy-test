module tech.sirwellington.alchemy.test {
    requires alchemy.generator;
    requires java.sql;
    requires junit;
    requires org.mockito;
    requires org.hamcrest;
    requires org.slf4j;
    requires tech.sirwellington.alchemy.annotations;
    requires tech.sirwellington.alchemy.generator;

    exports tech.sirwellington.alchemy.test;
}