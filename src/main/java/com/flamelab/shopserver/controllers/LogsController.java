package com.flamelab.shopserver.controllers;

import com.flamelab.shopserver.dtos.transfer.TransferAuthTokenDto;
import com.flamelab.shopserver.managers.AuthManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.flamelab.shopserver.enums.Roles.ADMIN;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogsController extends BaseController {

    private final AuthManager authManager;

    @GetMapping("/logText1")
    public ResponseEntity<?> getLogText1(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity
                .status(OK)
                .body(provideLogText(authManager.validateAuthToken(authorization, ADMIN())));
    }

    private String provideLogText(TransferAuthTokenDto tokenDto) {
        return String.format("2023-04-23T17:49:14.162+03:00 ERROR 4660 --- [           main] o.s.test.context.TestContextManager      : Caught exception while allowing TestExecutionListener [org.springframework.test.context.web.ServletTestExecutionListener] to p\n" +
                "repare test instance [com.flamelab.shopserver.ShopServerApplicationTests@7beae796]\n" +
                "\n" +
                "java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@6b63e6ad testClass = com.flamelab.shopserver.ShopServerApplicationTests, locations = [], classes = [com.flamelab.shopserver.ShopSe\n" +
                "rverApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceLocations = [], propertySourceProperties = [\"org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true\"], contextCustomizers = [\n" +
                "org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@506ae4d4, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@52b1beb6, org.springframework.\n" +
                "boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@6221a451, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizer\n" +
                "Factory$DisableObservabilityContextCustomizer@9da1, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory\n" +
                "$Customizer@2ddc9a9f, org.springframework.boot.test.context.SpringBootTestAnnotation@5afecf43], resourceBasePath = \"src/main/webapp\", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]     \n" +
                "        at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:142) ~[spring-test-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:127) ~[spring-test-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.test.context.web.ServletTestExecutionListener.setUpRequestContextIfNecessary(ServletTestExecutionListener.java:191) ~[spring-test-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.test.context.web.ServletTestExecutionListener.prepareTestInstance(ServletTestExecutionListener.java:130) ~[spring-test-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.test.context.TestContextManager.prepareTestInstance(TestContextManager.java:241) ~[spring-test-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.test.context.junit.jupiter.SpringExtension.postProcessTestInstance(SpringExtension.java:138) ~[spring-test-6.0.8.jar:6.0.8]\n" +
                "        at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$invokeTestInstancePostProcessors$10(ClassBasedTestDescriptor.java:377) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.executeAndMaskThrowable(ClassBasedTestDescriptor.java:382) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$invokeTestInstancePostProcessors$11(ClassBasedTestDescriptor.java:377) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197) ~[na:na]\n" +
                "        at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:179) ~[na:na]\n" +
                "        at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625) ~[na:na]\n" +
                "        at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509) ~[na:na]\n" +
                "        at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499) ~[na:na]\n" +
                "        at java.base/java.util.stream.StreamSpliterators$WrappingSpliterator.forEachRemaining(StreamSpliterators.java:310) ~[na:na]\n" +
                "        at java.base/java.util.stream.Streams$ConcatSpliterator.forEachRemaining(Streams.java:735) ~[na:na]\n" +
                "        at java.base/java.util.stream.Streams$ConcatSpliterator.forEachRemaining(Streams.java:734) ~[na:na]\n" +
                "        at java.base/java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:762) ~[na:na]\n" +
                "        at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.invokeTestInstancePostProcessors(ClassBasedTestDescriptor.java:376) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$instantiateAndPostProcessTestInstance$6(ClassBasedTestDescriptor.java:289) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.instantiateAndPostProcessTestInstance(ClassBasedTestDescriptor.java:288) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$testInstancesProvider$4(ClassBasedTestDescriptor.java:278) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at java.base/java.util.Optional.orElseGet(Optional.java:364) ~[na:na]\n" +
                "        at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$testInstancesProvider$5(ClassBasedTestDescriptor.java:277) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.jupiter.engine.execution.TestInstancesProvider.getTestInstances(TestInstancesProvider.java:31) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$prepare$0(TestMethodTestDescriptor.java:105) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.prepare(TestMethodTestDescriptor.java:104) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.prepare(TestMethodTestDescriptor.java:68) ~[junit-jupiter-engine-5.9.2.jar:5.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$prepare$2(NodeTestTask.java:123) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.prepare(NodeTestTask.java:123) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:90) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at java.base/java.util.ArrayList.forEach(ArrayList.java:1511) ~[na:na]\n" +
                "        at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at java.base/java.util.ArrayList.forEach(ArrayList.java:1511) ~[na:na]\n" +
                "        at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:35) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:54) ~[junit-platform-engine-1.9.2.jar:1.9.2]\n" +
                "        at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:220) ~[junit-platform-launcher-1.3.1.jar:1.3.1]\n" +
                "        at org.junit.platform.launcher.core.DefaultLauncher.lambda$execute$6(DefaultLauncher.java:188) ~[junit-platform-launcher-1.3.1.jar:1.3.1]\n" +
                "        at org.junit.platform.launcher.core.DefaultLauncher.withInterceptedStreams(DefaultLauncher.java:202) ~[junit-platform-launcher-1.3.1.jar:1.3.1]\n" +
                "        at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:181) ~[junit-platform-launcher-1.3.1.jar:1.3.1]\n" +
                "        at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:128) ~[junit-platform-launcher-1.3.1.jar:1.3.1]\n" +
                "        at org.apache.maven.surefire.junitplatform.JUnitPlatformProvider.invokeAllTests(JUnitPlatformProvider.java:150) ~[surefire-junit-platform-2.22.2.jar:2.22.2]\n" +
                "        at org.apache.maven.surefire.junitplatform.JUnitPlatformProvider.invoke(JUnitPlatformProvider.java:124) ~[surefire-junit-platform-2.22.2.jar:2.22.2]\n" +
                "        at org.apache.maven.surefire.booter.ForkedBooter.invokeProviderInSameClassLoader(ForkedBooter.java:384) ~[surefire-booter-2.22.2.jar:2.22.2]\n" +
                "        at org.apache.maven.surefire.booter.ForkedBooter.runSuitesInProcess(ForkedBooter.java:345) ~[surefire-booter-2.22.2.jar:2.22.2]\n" +
                "        at org.apache.maven.surefire.booter.ForkedBooter.execute(ForkedBooter.java:126) ~[surefire-booter-2.22.2.jar:2.22.2]\n" +
                "        at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:418) ~[surefire-booter-2.22.2.jar:2.22.2]\n" +
                "Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'dataSourceScriptDatabaseInitializer' defined in class path resource [org/springframework/boot/autoconfigure/sql/init/DataSou\n" +
                "rceInitializationConfiguration.class]: Unsatisfied dependency expressed through method 'dataSourceScriptDatabaseInitializer' parameter 0: Error creating bean with name 'dataSource' defined in class path resource [org/springframework\n" +
                "/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to load driver class org.postgresql.Driver \n" +
                "in either of HikariConfig class loader or Thread context classloader - User id: %s\n" +
                "        at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:800) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:550) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1332) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1162) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:560) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:520) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:326) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:324) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:313) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1132) ~[spring-context-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:907) ~[spring-context-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:584) ~[spring-context-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:732) ~[spring-boot-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:434) ~[spring-boot-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.SpringApplication.run(SpringApplication.java:310) ~[spring-boot-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.test.context.SpringBootContextLoader.lambda$loadContext$3(SpringBootContextLoader.java:137) ~[spring-boot-test-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:58) ~[spring-core-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:46) ~[spring-core-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.boot.SpringApplication.withHook(SpringApplication.java:1388) ~[spring-boot-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.test.context.SpringBootContextLoader$ContextLoaderHook.run(SpringBootContextLoader.java:545) ~[spring-boot-test-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:137) ~[spring-boot-test-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:108) ~[spring-boot-test-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:184) ~[spring-test-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:118) ~[spring-test-6.0.8.jar:6.0.8]\n" +
                "        ... 67 common frames omitted\n" +
                "Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]: Failed\n" +
                " to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to load driver class org.postgresql.Driver in either of HikariConfig class loader or Thread context classloader  \n" +
                "        at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:659) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:647) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1332) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1162) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:560) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:520) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:326) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:324) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1417) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1337) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:887) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:791) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        ... 93 common frames omitted\n" +
                "Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to load driver class org.postgresql.Driver\n" +
                " in either of HikariConfig class loader or Thread context classloader\n" +
                "        at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:171) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:655) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        ... 107 common frames omitted\n" +
                "Caused by: java.lang.RuntimeException: Failed to load driver class org.postgresql.Driver in either of HikariConfig class loader or Thread context classloader\n" +
                "        at com.zaxxer.hikari.HikariConfig.setDriverClassName(HikariConfig.java:488) ~[HikariCP-5.0.1.jar:na]\n" +
                "        at org.springframework.boot.jdbc.DataSourceBuilder$MappedDataSourceProperty.set(DataSourceBuilder.java:479) ~[spring-boot-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.jdbc.DataSourceBuilder$MappedDataSourceProperties.set(DataSourceBuilder.java:373) ~[spring-boot-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.jdbc.DataSourceBuilder.build(DataSourceBuilder.java:183) ~[spring-boot-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration.createDataSource(DataSourceConfiguration.java:48) ~[spring-boot-autoconfigure-3.0.6.jar:3.0.6]\n" +
                "        at org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration$Hikari.dataSource(DataSourceConfiguration.java:90) ~[spring-boot-autoconfigure-3.0.6.jar:3.0.6]\n" +
                "        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]\n" +
                "        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]\n" +
                "        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]\n" +
                "        at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]\n" +
                "        at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:139) ~[spring-beans-6.0.8.jar:6.0.8]\n" +
                "        ... 108 common frames omitted\n" +
                "\n" +
                "[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 1.899 s <<< FAILURE! - in com.flamelab.shopserver.ShopServerApplicationTests\n" +
                "[ERROR] contextLoads  Time elapsed: 0.001 s  <<< ERROR!\n" +
                "java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@6b63e6ad testClass = com.flamelab.shopserver.ShopServerApplicationTests, locations = [], classes = [com.flamelab.shopserver.ShopSe\n" +
                "rverApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceLocations = [], propertySourceProperties = [\"org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true\"], contextCustomizers = [\n" +
                "org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@506ae4d4, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@52b1beb6, org.springframework.\n" +
                "boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@6221a451, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizer\n" +
                "Factory$DisableObservabilityContextCustomizer@9da1, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory\n" +
                "$Customizer@2ddc9a9f, org.springframework.boot.test.context.SpringBootTestAnnotation@5afecf43], resourceBasePath = \"src/main/webapp\", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]     \n" +
                "Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'dataSourceScriptDatabaseInitializer' defined in class path resource [org/springframework/boot/autoconfigure/sql/init/DataSou\n" +
                "rceInitializationConfiguration.class]: Unsatisfied dependency expressed through method 'dataSourceScriptDatabaseInitializer' parameter 0: Error creating bean with name 'dataSource' defined in class path resource [org/springframework\n" +
                "/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]: User email: %s Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to load driver class org.postgresql.Driver \n" +
                "in either of HikariConfig class loader or Thread context classloader\n" +
                "Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]: Failed\n" +
                " to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to load driver class org.postgresql.Driver in either of HikariConfig class loader or Thread context classloader  \n" +
                "Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to load driver class org.postgresql.Driver\n" +
                " in either of HikariConfig class loader or Thread context classloader\n" +
                "Caused by: java.lang.RuntimeException: Failed to load driver class org.postgresql.Driver in either of HikariConfig class loader or Thread context classloader\n" +
                "\n" +
                "[INFO] \n" +
                "[INFO] Results:\n" +
                "[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:2.22.2:test (default-test) on project shop-server: There are test failures.\n" +
                "[ERROR]\n" +
                "[ERROR] Please refer to G:\\QA\\MyCourse\\projects\\shop\\shop-server\\target\\surefire-reports for the individual test results.\n" +
                "[ERROR] Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.\n" +
                "[ERROR] -> [Help 1]\n" +
                "[ERROR]\n" +
                "[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.\n" +
                "[ERROR] Re-run Maven using the -X switch to enable full debug logging.\n" +
                "[ERROR]\n" +
                "[ERROR] For more information about the errors and possible solutions, please read the following articles:\n" +
                "[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException\nSome log text with data.", tokenDto.getUserId(), tokenDto.getEmail());
    }

}
