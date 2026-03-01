package uz.stajirovka.jbooking.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import uz.stajirovka.jbooking.ui.view.HotelExplorerView;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        DrawerToggle drawerToggle = new DrawerToggle();

        H1 title = new H1("J-Booking");
        title.getStyle()
            .set("font-size", "1.4rem")
            .set("margin", "0")
            .set("color", "#0b3b66");

        HorizontalLayout topBar = new HorizontalLayout(drawerToggle, title);
        topBar.setWidthFull();
        topBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        topBar.expand(title);
        topBar.getStyle()
            .set("padding", "0.75rem 1rem")
            .set("background", "linear-gradient(90deg, #e6f0ff, #d8f3e8)")
            .set("border-bottom", "1px solid #c9ddf5");

        Header header = new Header(topBar);
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink hotelsLink = new RouterLink("Hotel Explorer", HotelExplorerView.class);
        hotelsLink.setHighlightCondition(HighlightConditions.sameLocation());

        Tab hotelsTab = new Tab(hotelsLink);
        Tabs tabs = new Tabs(hotelsTab);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        Scroller scroller = new Scroller(tabs);
        scroller.setSizeFull();
        addToDrawer(scroller);
    }
}
