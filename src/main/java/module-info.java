module tech.sirwellington.alchemy.test {
    requires java.base;
    requires java.sql;
    requires org.junit.jupiter;
    requires org.slf4j;
    requires tech.sirwellington.alchemy.annotations;
    requires tech.sirwellington.alchemy.generator;

    exports tech.sirwellington.alchemy.test;
    exports tech.sirwellington.alchemy.test.generation;

    opens tech.sirwellington.alchemy.test to org.junit.platform.commons;
}