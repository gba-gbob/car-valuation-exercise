package org.identity.pico;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.picocontainer.PicoFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apiguardian.api.API;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.behaviors.Cached;
import org.picocontainer.lifecycle.DefaultLifecycleState;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@API(
        status = API.Status.STABLE
)


public final class CustomPicoFactory implements ObjectFactory {

    private final Set<Class<?>> classes = new HashSet();
    private final Set<Object> instances = new HashSet();
    private MutablePicoContainer pico;

    //Custom pico factory to inject drivers based on configuration
    public CustomPicoFactory() {
        String browser = System.getProperty("browser", "chrome");
        switch (browser) {
            case "chrome":
                instances.add(WebDriverManager.getInstance(ChromeDriver.class).create());
                break;
            case "chromium":
                instances.add(WebDriverManager.getInstance(ChromiumDriver.class).create());
                break;
            case "firefox":
                instances.add(WebDriverManager.getInstance(FirefoxDriver.class).create());
                break;

            case "edge":
                instances.add(WebDriverManager.getInstance(EdgeDriver.class).create());
                break;

            case "safari":
                instances.add(WebDriverManager.getInstance(SafariDriver.class).create());
                break;
            default:
                throw new RuntimeException("Unsupported browser: " + browser);
        }
    }


    private static boolean isInstantiable(Class<?> clazz) {
        boolean isNonStaticInnerClass = !Modifier.isStatic(clazz.getModifiers()) && clazz.getEnclosingClass() != null;
        return Modifier.isPublic(clazz.getModifiers()) && !Modifier.isAbstract(clazz.getModifiers()) && !isNonStaticInnerClass;
    }

    public void start() {

        if (this.pico == null) {
            this.pico = (new PicoBuilder()).withCaching().withLifecycle().build();

            for (Class<?> clazz : this.classes) {
                this.pico.addComponent(clazz);
            }
            for (Object instance : this.instances) {
                this.pico.addComponent(instance);
            }
        } else {
            this.pico.setLifecycleState(new DefaultLifecycleState());
            this.pico.getComponentAdapters().forEach((cached) -> ((Cached) cached).flush());
        }

        this.pico.start();

    }

    public void stop() {

        this.pico.stop();
        this.pico.dispose();

    }

    public boolean addClass(Class<?> clazz) {
        if (isInstantiable(clazz) && this.classes.add(clazz)) {
            this.addConstructorDependencies(clazz);
        }
        return true;
    }

    public <T> T getInstance(Class<T> type) {
        return (T) this.pico.getComponent(type);
    }

    private void addConstructorDependencies(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            for (Class<?> paramClazz : constructor.getParameterTypes()) {
                this.addClass(paramClazz);
            }
        }

    }
}