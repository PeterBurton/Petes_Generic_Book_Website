package ie.cit.oossp;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ie.cit.oossp.ui.AdminPage;
import ie.cit.oossp.ui.LoginPage;
import ie.cit.oossp.ui.UserPage;

@SuppressWarnings("serial")
@Theme("valo")
@SpringUI
@SpringViewDisplay
public class BookWebsiteUI extends UI implements ViewDisplay {

    private Panel springViewDisplay;

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);
        root.setSpacing(false);
        
        final Label siteLabel = new Label ("Pete's Generic Book Website");
        siteLabel.setStyleName("h1");
        siteLabel.setWidthUndefined();
        siteLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponent(createNavigationButton("Login Page",
                LoginPage.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Admin Page",
                AdminPage.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("User Page",
                UserPage.VIEW_NAME));
        root.addComponents(siteLabel, navigationBar);
        root.setComponentAlignment(siteLabel, Alignment.TOP_CENTER);
        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);

    }

    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    @Override
    public void showView(View view) {
        springViewDisplay.setContent((Component) view);
    }
}