package uz.stajirovka.jbooking.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.PageRequest;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.request.BookingPaymentRequest;
import uz.stajirovka.jbooking.dto.request.GuestInfoRequest;
import uz.stajirovka.jbooking.dto.request.HotelReviewRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.CityResponse;
import uz.stajirovka.jbooking.dto.response.HotelResponse;
import uz.stajirovka.jbooking.dto.response.HotelReviewResponse;
import uz.stajirovka.jbooking.dto.response.HotelReviewsResponse;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.service.BookingService;
import uz.stajirovka.jbooking.service.CityService;
import uz.stajirovka.jbooking.service.HotelReviewService;
import uz.stajirovka.jbooking.service.HotelService;
import uz.stajirovka.jbooking.service.RoomService;
import uz.stajirovka.jbooking.ui.MainLayout;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Route(value = "", layout = MainLayout.class)
@PageTitle("J-Booking | Vaadin")
public class HotelExplorerView extends VerticalLayout {

    private static final String HERO_IMAGE =
        "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1600&q=80";
    private static final String HOTEL_CARD_IMAGE =
        "https://images.unsplash.com/photo-1455587734955-081b22074882?auto=format&fit=crop&w=1200&q=80";
    private static final String ROOM_CARD_IMAGE =
        "https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=1200&q=80";
    private static final String REVIEW_CARD_IMAGE =
        "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=1200&q=80";
    private static final List<String> DESTINATION_IMAGES = List.of(
        "https://images.unsplash.com/photo-1518684079-3c830dcef090?auto=format&fit=crop&w=900&q=80",
        "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=900&q=80",
        "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=900&q=80"
    );

    private final CityService cityService;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final BookingService bookingService;
    private final HotelReviewService hotelReviewService;

    private final ComboBox<CityResponse> cityBox = new ComboBox<>("City");
    private final DateTimePicker checkIn = new DateTimePicker("Check-in");
    private final DateTimePicker checkOut = new DateTimePicker("Check-out");
    private final IntegerField guestsField = new IntegerField("Guests");
    private final IntegerField minStarsField = new IntegerField("Min stars");
    private final NumberField minPriceField = new NumberField("Min price");
    private final NumberField maxPriceField = new NumberField("Max price");
    private final Select<Currency> currencySelect = new Select<>();
    private final com.vaadin.flow.component.combobox.MultiSelectComboBox<Amenity> amenitiesBox =
        new com.vaadin.flow.component.combobox.MultiSelectComboBox<>("Amenities");

    private final Grid<HotelResponse> hotelsGrid = new Grid<>(HotelResponse.class, false);
    private final Grid<RoomResponse> roomsGrid = new Grid<>(RoomResponse.class, false);
    private final Div bookingStatusCard = new Div();
    private final TextField guestFirstName = new TextField("Guest first name");
    private final TextField guestLastName = new TextField("Guest last name");
    private final TextField guestPinfl = new TextField("PINFL (14 digits)");
    private final EmailField guestEmail = new EmailField("Guest email");
    private final TextField guestPhone = new TextField("Guest phone");
    private final Select<Currency> paymentCurrency = new Select<>();
    private final TextField senderNameField = new TextField("Payer name");
    private final TextField senderTokenField = new TextField("Payer token");
    private final Button createBookingButton = new Button("Create Booking");
    private final Button confirmBookingButton = new Button("Confirm Booking");
    private final Button payBookingButton = new Button("Pay Booking");
    private final Button cancelBookingButton = new Button("Cancel Booking");
    private final Grid<HotelReviewResponse> reviewsGrid = new Grid<>(HotelReviewResponse.class, false);
    private final Div reviewSummaryCard = new Div();
    private final NumberField reviewGuestIdField = new NumberField("Guest ID");
    private final IntegerField reviewRatingField = new IntegerField("Rating (1-5)");
    private final TextArea reviewDescriptionField = new TextArea("Review text");
    private final Button submitReviewButton = new Button("Submit Review");
    private final Button refreshReviewsButton = new Button("Refresh Reviews");

    private Long selectedHotelId;
    private Long selectedRoomId;
    private Long activeBookingId;

    public HotelExplorerView(CityService cityService, HotelService hotelService, RoomService roomService, BookingService bookingService,
                             HotelReviewService hotelReviewService) {
        this.cityService = cityService;
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.hotelReviewService = hotelReviewService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        getStyle()
            .set("background", "linear-gradient(180deg, #f5f9ff 0%, #eef9f2 100%)")
            .set("color", "#17324d");

        add(buildHeader(), buildInspirationStrip(), buildFilters(), buildContent(), buildBookingPanel(), buildReviewsPanel());
        loadCities();
        loadHotelsBySelectedCity();
    }

    private Div buildHeader() {
        H2 title = new H2("Hotel Explorer");
        title.getStyle()
            .set("margin", "0")
            .set("color", "#ffffff")
            .set("text-shadow", "0 2px 8px rgba(0,0,0,0.35)");

        Paragraph subtitle = new Paragraph("Find hotels and rooms in one place with a visual, travel-style dashboard.");
        subtitle.getStyle()
            .set("margin", "0.25rem 0 0")
            .set("color", "#ffffff")
            .set("text-shadow", "0 2px 8px rgba(0,0,0,0.3)");

        Span badge = new Span("Live data from your backend");
        badge.getStyle()
            .set("display", "inline-block")
            .set("margin-top", "0.85rem")
            .set("padding", "0.4rem 0.7rem")
            .set("border-radius", "999px")
            .set("background", "rgba(255,255,255,0.82)")
            .set("font-weight", "600")
            .set("color", "#17324d");

        Div header = new Div(title, subtitle, badge);
        header.getStyle()
            .set("padding", "1.4rem 1.6rem")
            .set("border-radius", "18px")
            .set("background-image", "linear-gradient(95deg, rgba(15,47,74,0.62), rgba(17,77,66,0.46)), url('" + HERO_IMAGE + "')")
            .set("background-size", "cover")
            .set("background-position", "center")
            .set("box-shadow", "0 14px 35px rgba(12, 54, 93, 0.2)");
        return header;
    }

    private HorizontalLayout buildInspirationStrip() {
        Image first = createDecorImage(DESTINATION_IMAGES.get(0), "Destination");
        Image second = createDecorImage(DESTINATION_IMAGES.get(1), "Destination");
        Image third = createDecorImage(DESTINATION_IMAGES.get(2), "Destination");

        HorizontalLayout strip = new HorizontalLayout(first, second, third);
        strip.setWidthFull();
        strip.setSpacing(true);
        strip.setPadding(false);
        strip.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
        strip.expand(first, second, third);
        return strip;
    }

    private Image createDecorImage(String url, String alt) {
        Image image = new Image(url, alt);
        image.setWidthFull();
        image.setHeight("125px");
        image.getStyle()
            .set("object-fit", "cover")
            .set("border-radius", "14px")
            .set("box-shadow", "0 8px 20px rgba(12, 54, 93, 0.12)");
        return image;
    }

    private Div buildFilters() {
        cityBox.setItemLabelGenerator(CityResponse::name);
        cityBox.setWidth("240px");
        cityBox.addValueChangeListener(event -> loadHotelsBySelectedCity());

        checkIn.setValue(LocalDateTime.now().plusDays(1).withMinute(0).withSecond(0).withNano(0));
        checkOut.setValue(LocalDateTime.now().plusDays(3).withMinute(0).withSecond(0).withNano(0));
        checkIn.setStep(java.time.Duration.ofMinutes(30));
        checkOut.setStep(java.time.Duration.ofMinutes(30));

        guestsField.setValue(2);
        guestsField.setMin(1);
        guestsField.setMax(10);

        minStarsField.setMin(1);
        minStarsField.setMax(5);
        minStarsField.setValue(3);

        minPriceField.setMin(0);
        maxPriceField.setMin(0);

        amenitiesBox.setItems(Amenity.values());

        currencySelect.setLabel("Room currency");
        currencySelect.setItems(Currency.values());
        currencySelect.setValue(Currency.UZS);

        Button searchButton = new Button("Search");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.addClickListener(event -> searchHotels());

        HorizontalLayout row1 = new HorizontalLayout(cityBox, checkIn, checkOut, guestsField);
        row1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        row1.setWrap(true);

        HorizontalLayout row2 = new HorizontalLayout(minStarsField, minPriceField, maxPriceField, amenitiesBox, currencySelect, searchButton);
        row2.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        row2.setWrap(true);

        Div card = new Div(row1, row2);
        card.getStyle()
            .set("padding", "1rem 1.25rem")
            .set("border-radius", "14px")
            .set("background", "#ffffff")
            .set("box-shadow", "0 10px 30px rgba(12, 54, 93, 0.08)");
        return card;
    }

    private HorizontalLayout buildContent() {
        configureHotelsGrid();
        configureRoomsGrid();

        Image hotelsCover = createDecorImage(HOTEL_CARD_IMAGE, "Hotels");
        hotelsCover.setHeight("140px");
        VerticalLayout hotelsCard = new VerticalLayout(new H2("Hotels"), hotelsCover, hotelsGrid);
        hotelsCard.setPadding(false);
        hotelsCard.setSpacing(true);
        hotelsCard.setSizeFull();
        hotelsCard.getStyle()
            .set("padding", "1rem 1.25rem")
            .set("border-radius", "14px")
            .set("background", "#ffffff")
            .set("box-shadow", "0 10px 30px rgba(12, 54, 93, 0.08)");

        Image roomsCover = createDecorImage(ROOM_CARD_IMAGE, "Rooms");
        roomsCover.setHeight("140px");
        VerticalLayout roomsCard = new VerticalLayout(new H2("Rooms of selected hotel"), roomsCover, roomsGrid);
        roomsCard.setPadding(false);
        roomsCard.setSpacing(true);
        roomsCard.setSizeFull();
        roomsCard.getStyle()
            .set("padding", "1rem 1.25rem")
            .set("border-radius", "14px")
            .set("background", "#ffffff")
            .set("box-shadow", "0 10px 30px rgba(12, 54, 93, 0.08)");

        HorizontalLayout content = new HorizontalLayout(hotelsCard, roomsCard);
        content.setSizeFull();
        content.expand(hotelsCard, roomsCard);
        content.setAlignItems(FlexComponent.Alignment.STRETCH);
        return content;
    }

    private void configureHotelsGrid() {
        hotelsGrid.addComponentColumn(this::createHotelThumbnail)
            .setHeader("Photo")
            .setAutoWidth(true)
            .setFlexGrow(0);
        hotelsGrid.addColumn(HotelResponse::id).setHeader("ID").setAutoWidth(true);
        hotelsGrid.addColumn(HotelResponse::name).setHeader("Name").setAutoWidth(true);
        hotelsGrid.addColumn(HotelResponse::cityName).setHeader("City").setAutoWidth(true);
        hotelsGrid.addColumn(HotelResponse::stars).setHeader("Stars").setAutoWidth(true);
        hotelsGrid.addColumn(hotel -> hotel.accommodationType() == null ? "" : hotel.accommodationType().name())
            .setHeader("Type")
            .setAutoWidth(true);
        hotelsGrid.addColumn(HotelResponse::description).setHeader("Description").setFlexGrow(1);
        hotelsGrid.setHeight("520px");

        hotelsGrid.addSelectionListener(event -> {
            HotelResponse hotel = event.getFirstSelectedItem().orElse(null);
            if (hotel != null && cityBox.getValue() != null) {
                selectedHotelId = hotel.id();
                selectedRoomId = null;
                loadRooms(cityBox.getValue().id(), hotel.id());
                loadReviewsForSelectedHotel();
            } else {
                selectedHotelId = null;
                selectedRoomId = null;
                roomsGrid.setItems(Collections.emptyList());
                resetBookingState();
                resetReviewsState();
            }
        });
    }

    private Image createHotelThumbnail(HotelResponse hotel) {
        Image image = new Image(resolveHotelImageUrl(hotel), hotel.name());
        image.setWidth("88px");
        image.setHeight("56px");
        image.getStyle()
            .set("object-fit", "cover")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 12px rgba(12, 54, 93, 0.15)");
        return image;
    }

    private String resolveHotelImageUrl(HotelResponse hotel) {
        String city = hotel.cityName() == null ? "" : hotel.cityName().toLowerCase();
        if (city.contains("tashkent")) {
            return "https://images.unsplash.com/photo-1551776235-dde6d4829808?auto=format&fit=crop&w=800&q=80";
        }
        if (city.contains("samarkand")) {
            return "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=800&q=80";
        }
        if (city.contains("bukhara")) {
            return "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=800&q=80";
        }
        return "https://images.unsplash.com/photo-1501117716987-c8e1ecb2101d?auto=format&fit=crop&w=800&q=80";
    }

    private void configureRoomsGrid() {
        roomsGrid.addColumn(RoomResponse::id).setHeader("ID").setAutoWidth(true);
        roomsGrid.addColumn(RoomResponse::roomNumber).setHeader("Room").setAutoWidth(true);
        roomsGrid.addColumn(room -> room.roomType() == null ? "" : room.roomType().name()).setHeader("Type");
        roomsGrid.addColumn(RoomResponse::capacity).setHeader("Capacity");
        roomsGrid.addColumn(RoomResponse::pricePerNight).setHeader("Price/night");
        roomsGrid.addColumn(room -> room.mealPlan() == null ? "" : room.mealPlan().name()).setHeader("Meal");
        roomsGrid.addColumn(room -> room.amenities() == null ? "" : String.join(", ", room.amenities().stream().map(Enum::name).toList()))
            .setHeader("Amenities")
            .setFlexGrow(1);
        roomsGrid.setHeight("520px");

        roomsGrid.addSelectionListener(event -> {
            RoomResponse room = event.getFirstSelectedItem().orElse(null);
            selectedRoomId = room == null ? null : room.id();
            if (selectedRoomId == null) {
                resetBookingState();
            } else {
                setBookingStatus("Room selected: #" + selectedRoomId + ". Fill guest data and create booking.");
                createBookingButton.setEnabled(true);
            }
        });
    }

    private void loadCities() {
        List<CityResponse> cities = cityService.getAll(PageRequest.of(0, 100)).getContent();
        cityBox.setItems(cities);
        if (!cities.isEmpty()) {
            cityBox.setValue(cities.getFirst());
        }
    }

    private void loadHotelsBySelectedCity() {
        CityResponse selectedCity = cityBox.getValue();
        if (selectedCity == null) {
            hotelsGrid.setItems(Collections.emptyList());
            roomsGrid.setItems(Collections.emptyList());
            return;
        }
        List<HotelResponse> hotels = hotelService.getByCityId(selectedCity.id(), PageRequest.of(0, 100)).getContent();
        hotelsGrid.setItems(hotels);
        roomsGrid.setItems(Collections.emptyList());
        selectedHotelId = null;
        selectedRoomId = null;
        resetBookingState();
        resetReviewsState();
    }

    private void searchHotels() {
        LocalDateTime start = checkIn.getValue();
        LocalDateTime end = checkOut.getValue();
        if (start == null || end == null || !end.isAfter(start)) {
            Notification notification = Notification.show("Check search dates.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Long minPrice = minPriceField.getValue() == null ? null : minPriceField.getValue().longValue();
        Long maxPrice = maxPriceField.getValue() == null ? null : maxPriceField.getValue().longValue();
        Integer minStars = minStarsField.getValue();
        Integer guests = guestsField.getValue() == null ? 2 : guestsField.getValue();
        Long cityId = cityBox.getValue() == null ? null : cityBox.getValue().id();
        Set<Amenity> amenities = amenitiesBox.getValue().isEmpty() ? null : amenitiesBox.getValue();

        List<HotelResponse> hotels = hotelService.search(
            start,
            end,
            guests,
            cityId,
            minPrice,
            maxPrice,
            minStars,
            amenities,
            PageRequest.of(0, 100)
        ).getContent();

        hotelsGrid.setItems(hotels);
        roomsGrid.setItems(Collections.emptyList());
        selectedHotelId = null;
        selectedRoomId = null;
        resetBookingState();
        resetReviewsState();
    }

    private void loadRooms(Long cityId, Long hotelId) {
        List<RoomResponse> rooms = roomService.getByHotelId(cityId, hotelId, currencySelect.getValue(), PageRequest.of(0, 100)).getContent();
        roomsGrid.setItems(rooms);
        selectedRoomId = null;
        resetBookingState();
    }

    private VerticalLayout buildBookingPanel() {
        guestFirstName.setValue("John");
        guestLastName.setValue("Doe");
        guestPinfl.setValue("12345678901234");
        guestEmail.setValue("john.doe@example.com");
        guestPhone.setValue("+998901112233");

        paymentCurrency.setLabel("Payment currency");
        paymentCurrency.setItems(Currency.values());
        paymentCurrency.setValue(Currency.UZS);

        senderNameField.setValue("Demo Payer");
        senderTokenField.setValue("demo-token-123");

        createBookingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createBookingButton.setEnabled(false);
        createBookingButton.addClickListener(event -> createBooking());

        confirmBookingButton.setEnabled(false);
        confirmBookingButton.addClickListener(event -> confirmBooking());

        payBookingButton.setEnabled(false);
        payBookingButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        payBookingButton.addClickListener(event -> payBooking());

        cancelBookingButton.setEnabled(false);
        cancelBookingButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelBookingButton.addClickListener(event -> cancelBooking());

        bookingStatusCard.getStyle()
            .set("padding", "0.75rem 1rem")
            .set("border-radius", "12px")
            .set("background", "#f6fbff")
            .set("border", "1px solid #d9ebfb");
        setBookingStatus("Select hotel and room to start booking.");

        HorizontalLayout guestRow1 = new HorizontalLayout(guestFirstName, guestLastName, guestPinfl);
        guestRow1.setWidthFull();
        guestRow1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        HorizontalLayout guestRow2 = new HorizontalLayout(guestEmail, guestPhone);
        guestRow2.setWidthFull();
        guestRow2.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        HorizontalLayout paymentRow = new HorizontalLayout(paymentCurrency, senderNameField, senderTokenField);
        paymentRow.setWidthFull();
        paymentRow.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        HorizontalLayout actions = new HorizontalLayout(createBookingButton, confirmBookingButton, payBookingButton, cancelBookingButton);

        VerticalLayout bookingLayout = new VerticalLayout(
            new H2("Booking Actions"),
            bookingStatusCard,
            guestRow1,
            guestRow2,
            paymentRow,
            actions
        );
        bookingLayout.setWidthFull();
        bookingLayout.getStyle()
            .set("padding", "1rem 1.25rem")
            .set("border-radius", "14px")
            .set("background", "#ffffff")
            .set("box-shadow", "0 10px 30px rgba(12, 54, 93, 0.08)");
        return bookingLayout;
    }

    private VerticalLayout buildReviewsPanel() {
        configureReviewsGrid();

        reviewSummaryCard.getStyle()
            .set("padding", "0.75rem 1rem")
            .set("border-radius", "12px")
            .set("background", "#f5fff7")
            .set("border", "1px solid #d9f1df");
        setReviewSummary("Select a hotel to see and add reviews.");

        reviewGuestIdField.setMin(1);
        reviewGuestIdField.setStepButtonsVisible(true);
        reviewGuestIdField.setValue(1d);

        reviewRatingField.setMin(1);
        reviewRatingField.setMax(5);
        reviewRatingField.setValue(5);

        reviewDescriptionField.setPlaceholder("Share your experience...");
        reviewDescriptionField.setHeight("90px");

        submitReviewButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitReviewButton.setEnabled(false);
        submitReviewButton.addClickListener(event -> submitReview());

        refreshReviewsButton.setEnabled(false);
        refreshReviewsButton.addClickListener(event -> loadReviewsForSelectedHotel());

        HorizontalLayout formRow = new HorizontalLayout(reviewGuestIdField, reviewRatingField);
        formRow.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        HorizontalLayout actions = new HorizontalLayout(submitReviewButton, refreshReviewsButton);

        Image reviewsCover = createDecorImage(REVIEW_CARD_IMAGE, "Reviews");
        reviewsCover.setHeight("140px");

        VerticalLayout reviewsLayout = new VerticalLayout(
            new H2("Hotel Reviews"),
            reviewsCover,
            reviewSummaryCard,
            formRow,
            reviewDescriptionField,
            actions,
            reviewsGrid
        );
        reviewsLayout.setWidthFull();
        reviewsLayout.getStyle()
            .set("padding", "1rem 1.25rem")
            .set("border-radius", "14px")
            .set("background", "#ffffff")
            .set("box-shadow", "0 10px 30px rgba(12, 54, 93, 0.08)");
        return reviewsLayout;
    }

    private void configureReviewsGrid() {
        reviewsGrid.addColumn(HotelReviewResponse::id).setHeader("ID").setAutoWidth(true);
        reviewsGrid.addColumn(HotelReviewResponse::guestName).setHeader("Guest").setAutoWidth(true);
        reviewsGrid.addColumn(HotelReviewResponse::rating).setHeader("Rating").setAutoWidth(true);
        reviewsGrid.addColumn(HotelReviewResponse::description).setHeader("Comment").setFlexGrow(1);
        reviewsGrid.addColumn(review -> review.createdAt() == null ? "" : review.createdAt().toString())
            .setHeader("Created")
            .setAutoWidth(true);
        reviewsGrid.setHeight("320px");
    }

    private void loadReviewsForSelectedHotel() {
        if (selectedHotelId == null) {
            resetReviewsState();
            return;
        }
        try {
            HotelReviewsResponse response = hotelReviewService.getByHotelId(selectedHotelId, PageRequest.of(0, 50));
            reviewsGrid.setItems(response.reviews().getContent());
            String avg = response.averageRating() == null ? "n/a" : String.format("%.2f", response.averageRating());
            setReviewSummary("Hotel #" + selectedHotelId + ": " + response.reviewCount() + " reviews, average rating " + avg);
            submitReviewButton.setEnabled(true);
            refreshReviewsButton.setEnabled(true);
        } catch (Exception e) {
            showError("Load reviews failed: " + rootMessage(e));
        }
    }

    private void submitReview() {
        if (selectedHotelId == null) {
            showError("Select hotel first.");
            return;
        }
        if (reviewGuestIdField.getValue() == null || reviewGuestIdField.getValue() < 1) {
            showError("Guest ID is required.");
            return;
        }
        if (reviewRatingField.getValue() == null || reviewRatingField.getValue() < 1 || reviewRatingField.getValue() > 5) {
            showError("Rating must be from 1 to 5.");
            return;
        }
        HotelReviewRequest request = new HotelReviewRequest(
            reviewGuestIdField.getValue().longValue(),
            reviewRatingField.getValue(),
            reviewDescriptionField.getValue()
        );
        try {
            var response = hotelReviewService.create(selectedHotelId, request);
            Notification notification = Notification.show("Review submitted by " + response.guestName() + ".");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            reviewDescriptionField.clear();
            loadReviewsForSelectedHotel();
        } catch (Exception e) {
            showError("Submit review failed: " + rootMessage(e));
        }
    }

    private void setReviewSummary(String text) {
        reviewSummaryCard.removeAll();
        reviewSummaryCard.add(new Span(text));
    }

    private void resetReviewsState() {
        reviewsGrid.setItems(Collections.emptyList());
        submitReviewButton.setEnabled(false);
        refreshReviewsButton.setEnabled(false);
        setReviewSummary("Select a hotel to see and add reviews.");
    }

    private void createBooking() {
        CityResponse city = cityBox.getValue();
        if (city == null || selectedHotelId == null || selectedRoomId == null) {
            showError("Select city, hotel and room first.");
            return;
        }
        if (checkIn.getValue() == null || checkOut.getValue() == null || !checkOut.getValue().isAfter(checkIn.getValue())) {
            showError("Invalid check-in/check-out dates.");
            return;
        }

        GuestInfoRequest mainGuest = new GuestInfoRequest(
            guestFirstName.getValue(),
            guestLastName.getValue(),
            guestPinfl.getValue(),
            guestEmail.getValue(),
            guestPhone.getValue()
        );
        BookingCreateRequest request = new BookingCreateRequest(
            city.id(),
            selectedHotelId,
            selectedRoomId,
            checkIn.getValue(),
            checkOut.getValue(),
            mainGuest,
            List.of()
        );

        try {
            BookingResponse response = bookingService.initBooking(request);
            activeBookingId = response.id();
            setBookingStatus("Booking #" + response.id() + " created. Status: " + response.status());
            confirmBookingButton.setEnabled(true);
            payBookingButton.setEnabled(false);
            cancelBookingButton.setEnabled(true);
        } catch (Exception e) {
            showError("Create failed: " + rootMessage(e));
        }
    }

    private void confirmBooking() {
        if (activeBookingId == null) {
            showError("Create booking first.");
            return;
        }
        try {
            var response = bookingService.confirmBooking(new BookingConfirmRequest(activeBookingId));
            setBookingStatus("Booking #" + activeBookingId + " confirmed. Status: " + response.bookingStatus());
            payBookingButton.setEnabled(true);
        } catch (Exception e) {
            showError("Confirm failed: " + rootMessage(e));
        }
    }

    private void payBooking() {
        if (activeBookingId == null) {
            showError("Create booking first.");
            return;
        }
        try {
            BookingResponse booking = bookingService.getById(activeBookingId, paymentCurrency.getValue());
            BookingPaymentRequest request = new BookingPaymentRequest(
                activeBookingId,
                booking.totalPrice(),
                paymentCurrency.getValue(),
                senderNameField.getValue(),
                senderTokenField.getValue()
            );
            var response = bookingService.payConfirmedBooking(request);
            setBookingStatus("Payment complete for booking #" + activeBookingId + ". Status: "
                + response.bookingStatus() + ", transactionId: " + response.transactionId());
            payBookingButton.setEnabled(false);
            confirmBookingButton.setEnabled(false);
        } catch (Exception e) {
            showError("Pay failed: " + rootMessage(e));
        }
    }

    private void cancelBooking() {
        if (activeBookingId == null) {
            showError("No active booking to cancel.");
            return;
        }
        try {
            BookingResponse response = bookingService.cancel(activeBookingId);
            setBookingStatus("Booking #" + activeBookingId + " cancelled. Status: " + response.status());
            payBookingButton.setEnabled(false);
            confirmBookingButton.setEnabled(false);
        } catch (Exception e) {
            showError("Cancel failed: " + rootMessage(e));
        }
    }

    private void setBookingStatus(String text) {
        bookingStatusCard.removeAll();
        bookingStatusCard.add(new Span(text));
    }

    private void resetBookingState() {
        activeBookingId = null;
        createBookingButton.setEnabled(selectedRoomId != null);
        confirmBookingButton.setEnabled(false);
        payBookingButton.setEnabled(false);
        cancelBookingButton.setEnabled(false);
        if (selectedRoomId == null) {
            setBookingStatus("Select hotel and room to start booking.");
        }
    }

    private void showError(String message) {
        Notification notification = Notification.show(message);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private String rootMessage(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return root.getMessage() == null ? root.toString() : root.getMessage();
    }
}
