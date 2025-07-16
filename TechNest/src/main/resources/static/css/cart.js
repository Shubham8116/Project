$(document).ready(function() {




    // Declared the updateQuantity function within the global window object to ensure it’s accessible from the HTML elements.
    window.updateQuantity = function(cartId, quantity) {
        if (quantity < 1 || quantity > 3) return;
        $.ajax({
            url: '/update-cart',
            type: 'POST',
            data: { cartId: cartId, quantity: quantity },
            success: function(response) {
                location.reload(); // Reload the page to reflect the changes
            },
            error: function(error) {
                console.error('Error updating cart quantity:', error);
            }
        });
    }

    window.removeFromCart = function(cartId) {
        $.ajax({
            url: '/remove-from-cart',
            type: 'POST',
            data: { cartId: cartId },
            success: function(response) {
                showToast("Item removed successfully");
                setTimeout(() => {
                    location.reload();
                }, 1500); // give user time to see toast

            },
            error: function(error) {
                console.error('Error removing item from cart:', error);
            }
        });
    };

    function showToast(message) {
        const toast = document.getElementById("toast");
        toast.textContent = message;
        toast.classList.add("show");

        setTimeout(() => {
            toast.classList.remove("show");
        }, 1500); // hide after 1.5 seconds
    }



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

});
