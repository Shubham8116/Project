$(document).ready(function() {
    //filter product by brand
    $("input[name='manufacturer']").on("change", function () {
        const selectedManufacturer = $(this).val();

        // Get filter URL from hidden field or data attribute
        const filterUrl = $("#product-container").data("filter-url");

        $.ajax({
            url: filterUrl,
            type: "GET",
            data: { manufacturer: selectedManufacturer },
            success: function (data) {
                $("#product-container").html(data);
            },
            error: function () {
                $("#product-container").html("<p>Error loading products.</p>");
            }
        });
    });




    // Clock functionality with day and month
    function updateClock() {
        const now = new Date();

        const day = now.getDate().toString().padStart(2, '0');
        const month = now.toLocaleString('default', { month: 'long' }); // e.g., "July"
        const hours = now.getHours().toString().padStart(2, '0');
        const minutes = now.getMinutes().toString().padStart(2, '0');
        const seconds = now.getSeconds().toString().padStart(2, '0');

        const formatted = `${day} ${month} | ${hours}:${minutes}:${seconds}`;
        $('#clock').text(formatted);
    }

    setInterval(updateClock, 1000);
    updateClock();

    // Check if products are already in the cart
    $('.add-to-cart').each(function () {
        let productId = $(this).data('product-id');
        let button = $(this);
        $.ajax({
            url: '/check-cart',
            type: 'GET',
            data: { productId: productId },
            success: function (response) {
                if (response.inCart) {
                    button.text('Go to Cart');
                    button.attr('href', '/view-cart');
                    button.removeClass('add-to-cart').addClass('go-to-cart');
                }
            },
            error: function (error) {
                console.log("Error checking cart:", error);
            }
        });
    });

    // Event delegation to handle both add-to-cart and go-to-cart
    $(document).on('click', '.add-to-cart', function () {
        let productId = $(this).data('product-id');
        let button = $(this);
        $.ajax({
            url: '/add-to-cart',
            type: 'POST',
            data: { productId: productId },
            success: function (response) {
                alert("Product added to cart successfully!");
                button.text('Go to Cart');
                button.attr('href', '/view-cart');
                button.removeClass('add-to-cart').addClass('go-to-cart');
                updateCartCount(); // Update cart count
            },
            error: function (error) {
                alert("Error adding product to cart.");
            }
        });
    });

    $(document).on('click', '.go-to-cart', function () {
        window.location.href = '/view-cart';
    });

    // Function to update cart count
    function updateCartCount() {
        fetch('/count')
            .then(response => response.json())
            .then(data => {
                document.querySelector('.cart-count').textContent = data;
            })
            .catch(error => console.error('Error fetching cart count:', error));
    }

    // Initial call to update cart count on page load
    updateCartCount();
});



