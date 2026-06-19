module tech.sirwellington.alchemy.test {
    requires alchemy.generator;
    requires java.sql;
    requires junit;
    requires org.mockito;
    requires org.hamcrest;
    requires org.slf4j;
    requires tech.sirwellington.alchemy.annotations;

    exports tech.sirwellington.alchemy.test;
}