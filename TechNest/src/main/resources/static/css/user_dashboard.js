// Function to show the selected section and hide the others
function showSection(sectionId, event) {
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.style.display = 'none';  // Hide all sections
    });
    document.getElementById(sectionId).style.display = 'block';  // Show the selected section

    // Update active state of sidebar links
    const links = document.querySelectorAll('.sidebar ul li a');
    links.forEach(link => {
        link.classList.remove('active');
    });
    event.target.classList.add('active');
}

// Function to handle the view order button click
function viewOrder(orderId) {
    const containerId = '#order-item-details-container-' + orderId;
    const $container = $(containerId);

    if ($container.is(':empty')) {
        $.ajax({
            url: '/order-items',
            type: 'GET',
            data: { orderId: orderId },
            success: function(fragment) {
                $container.html(fragment).slideDown().removeClass('hidden');
            },
            error: function(err) {
                console.error('Failed to load order items:', err);
            }
        });
    } else {
        $container.slideToggle();
    }



}

// Function to handle the cancel order button click
function cancelOrder(orderId) {
    // Implement the logic to cancel the order
    if (confirm('Are you sure you want to cancel order ' + orderId + '?')) {
        $.ajax({
            url: '/cancel-order',
            type: 'POST',
            data: { orderId: orderId },
            success: function(response) {
                // showToast( 'Item has been canceled');
                // Disable the cancel button
                const $cancelBtn = $('.cancel-order-btn[data-order-id="' + orderId + '"]');
                $cancelBtn.prop('disabled', true)
                    .text('Cancelled')
                    .addClass('disabled-button');

                setTimeout(() => location.reload(), 1400); // reload to reflect status
            },
            error: function(error) {
                console.error('Error cancelling order:', error);
                showToast('Failed to cancel order. Please try again.');
            }
        });
    }

}


// Show default section (My Orders) on page load
// window.onload = function() {
//     showSection('my-orders', {target: document.querySelector('.sidebar ul li a[href="#my-orders"]')});
// };
