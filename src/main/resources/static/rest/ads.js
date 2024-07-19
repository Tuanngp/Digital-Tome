$(document).ready(function () {
    $('#ads-file').on('change', function () {
        showPreview(this);
    });

    loadAds(1);

    const urlParams = new URLSearchParams(window.location.search);
    const paymentStatus = urlParams.get('status');
    const adsId = urlParams.get('adsId');

    if (paymentStatus === 'success') {
        // checkPaymentStatus(adsId);
        notify("alert-success", "Payment successful. Campaign status updated.");
    } else if (paymentStatus === 'cancel') {
        notify("alert-warning", "Payment was cancelled");
    }

});
document.addEventListener('DOMContentLoaded', function () {
    loadAdsPackage().then(r => console.log('Ads package loaded'));
    const removeButton = document.getElementById('remove-file');
    const saveButton = document.getElementById('saveAd');
    const fileInput = document.getElementById('ads-file');

    saveButton.addEventListener('click', function () {
        const adTitle = document.getElementById('adTitle').value;
        const adContent = document.getElementById('adContent').value;
        const startDate = new Date(document.getElementById('startDate').value);
        const endDate = new Date(document.getElementById('endDate').value);
        const placementId = document.getElementById('placementId').value;
        const typeId = document.getElementById('typeId').value;
        const cost = document.getElementById('totalCost').textContent;
        const file = fileInput.files[0];
        const link = document.getElementById('adLink').value;

        if (!file) {
            alert('Vui lòng chọn hình ảnh');
            return;
        }

        const formData = new FormData();
        formData.append('title', adTitle);
        formData.append('content', adContent);
        formData.append('startDate', startDate.toISOString());
        formData.append('endDate', endDate.toISOString());
        formData.append('placementId', placementId);
        formData.append('typeId', typeId);
        formData.append('status', 'PENDING');
        formData.append('cost', cost);
        formData.append('file', file);
        formData.append('link', link);

        fetch('/api/ads/save', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            notify("alert-success", "Campaign saved successfully");
            clearModalData();
        })
        .catch(error => {
            console.error('Error:', error);
            notify("alert-danger", "Failed to save campaign data");
        });

    });

    removeButton.addEventListener('click', function () {
        fileInput.value = ''; // Clear the file input
        document.getElementById('image-preview').src = ''; // Clear the image preview if any
    });
});


function clearModalData() {
    // Reset các trường input
    document.getElementById('adTitle').value = '';
    document.getElementById('adLink').value = '';
    document.getElementById('adContent').value = '';
    document.getElementById('startDate').value = '';
    document.getElementById('endDate').value = '';

    // Reset các span
    document.getElementById('totalDays').textContent = '0';
    document.getElementById('totalCost').textContent = '0 VND';

    // Reset ảnh preview
    document.getElementById('image-preview').src = '';

    // Reset file input
    document.getElementById('ads-file').value = '';

    // Reset các input ẩn
    document.getElementById('typeId').value = '';
    document.getElementById('placementId').value = '';

    // Reset các lựa chọn gói quảng cáo (nếu có)
    var adsTypes = document.getElementById('ads-types');
    if (adsTypes) {
        var selectedItems = adsTypes.querySelectorAll('.selected');
        selectedItems.forEach(function(item) {
            item.classList.remove('selected');
        });
    }
}

function formatDate(date) {
    const d = new Date(date);
    const day = ('0' + d.getDate()).slice(-2);
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const year = d.getFullYear();
    return `${day}-${month}-${year}`;
}

function showPreview(selectFile) {
    let file = selectFile.files[0];
    let reader = new FileReader();

    reader.onload = function () {
        $('#image-preview').attr('src', reader.result);
    };

    if (file) {
        reader.readAsDataURL(file);
    }
}

let currentPage = 1;
let isLoading = false;
// Function to load books and pagination
function loadAds(page) {
    $.ajax({
        url: '/api/ads',
        method: 'GET',
        data: {
            page: page - 1,
            size: 5
        },
        dataType: 'json',
        success: function (data) {
            $('#adsList').empty();
            data.content.forEach(function (ads, index) {
                const row = $('<tr>');
                row.append('<td><div class="form-check custom-checkbox checkbox-success check-lg me-3">' +
                    '<input type="checkbox" class="form-check-input" id="customCheckBox' + index + '" required="">' +
                    '<label class="form-check-label" for="customCheckBox' + index + '"></label></div></td>');
                row.append('<td><strong>' + (ads.id || 'N/A') + '</strong></td>');
                row.append('<td id="adsName" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 100px;">' + (ads.title || 'N/A') + '</td>');
                row.append('<td><img src="' + (ads.imageUrl || '') + '" alt="image" style="max-height: 100px;"></td>');
                row.append('<td id="adsType">' + (ads.adsType || 'N/A') + '</td>');
                row.append('<td id="adsPlacement">' + (ads.adsPlacement || 'N/A') + '</td>');
                row.append('<td id="startDate">' + (ads.startDate ? formatDate(ads.startDate) : 'N/A') + '</td>');
                row.append('<td id="endDate">' + (ads.endDate ? formatDate(ads.endDate) : 'N/A') + '</td>');
                row.append('<td><div class="d-flex align-items-center">' +
                    '<i class="fa fa-circle ' +
                    (ads.status === 'PENDING' ? 'text-warning' :
                    ads.status === 'AWAITING_PAYMENT' ? 'text-primary' :
                    ads.status === 'ACTIVE' ? 'text-success' :
                    ads.status === 'COMPLETED' ? 'text-info' :
                    ads.status === 'CANCELLED' ? 'text-danger' :
                    ads.status === 'REJECTED' ? 'text-dark' : '') +
                    ' me-1"></i> ' + ads.status + '</div></td>');
                row.append('<td id="cost">' + (ads.cost.toLocaleString('vi-VN') + ' VND' || 'N/A') + '</td>');
                row.append('<td><div class="d-flex">' +
                    (ads.status === 'PENDING' ?
                        // `<button id="requestBtn" value="${ads.id}" class="btn btn-success shadow btn-md sharp me-1">Request</button>` +
                        `<button id="deleteBtn" value="${ads.id}" class="btn btn-danger shadow btn-md sharp me-1">Delete</button>` :
                    ads.status === 'AWAITING_PAYMENT' ?
                        `<button id="payBtn" value="${ads.id}" class="btn btn-primary shadow btn-md sharp me-1">Pay</button>` +
                        `<button id="deleteBtn" value="${ads.id}" class="btn btn-danger shadow btn-md sharp me-1">Delete</button>` :
                    ads.status === 'ACTIVE' ?
                        `<button id="activateBtn" value="${ads.id}" class="btn btn-info shadow btn-md sharp me-1">Deactivate</button>` :
                    ads.status === 'COMPLETED' ?
                    `<button id="viewBtn" value="${ads.id}" class="btn btn-light shadow btn-md sharp me-1">View</button>` :
                    ads.status === 'CANCELLED' ?
                        `<button id="relistBtn" value="${ads.id}" class="btn btn-warning shadow btn-md sharp me-1">Relist</button>` :
                    ads.status === 'REJECTED' ?
                        `<button id="appealBtn" value="${ads.id}" class="btn btn-dark shadow btn-md sharp me-1">Appeal</button>` :
                        '') +
                    '</div></td>');
                $('#adsList').append(row);

                // Attach click event to the "Reject" button
                row.find('#deleteBtn').on('click', function() {
                    const confirmAccept = confirm("Do you want to delete this campaign ?");
                    if (confirmAccept) {
                        $.ajax({
                            url: `/api/ads/delete/${ads.id}`,
                            method: 'DELETE',
                            success: function(response) {
                                loadAds(data.currentPage)
                                notify("alert-success", "Campaign deleted successfully")
                            },
                            error: function(jqXHR, textStatus, errorThrown) {
                                loadAds(data.currentPage)
                                notify("alert-danger", "Delete campaign unsuccessfully")
                            }
                        });
                    }
                });

                row.find('#payBtn').on('click', function() {
                    const confirmAccept = confirm("Do you want to pay for this campaign ?");
                    if (confirmAccept) {
                        const formData = new FormData();
                        formData.append('adsId', ads.adsId);
                        formData.append('amount', ads.cost);
                        formData.append('currency', 'USD');
                        formData.append('description', `Payment for campaign ${ads.title} from ${formatDate(ads.startDate)} to ${formatDate(ads.endDate)} at ${ads.adsPlacement} - ${ads.adsType}`);
                        fetch('/api/ads/payment/create', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Network response was not ok');
                                }
                                return response.json();
                            })
                            .then(data => {
                                window.location.href = data.approvalUrl;
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                notify("alert-danger", "Failed to initiate payment process");
                            });
                    }
                });
            });


            $('#pagination').twbsPagination({
                totalPages: data.totalPages,
                startPage: currentPage,
                visiblePages: 5,
                first: 'First',
                prev: '<<',
                next: '>>',
                last: 'Last',
                onPageClick: function (event, page) {
                    if (page !== currentPage) {
                        currentPage = page;
                        loadAds(page);
                    }
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error("Error loading Ads:", textStatus, errorThrown);
            console.error("Response Text:", jqXHR.responseText);
            console.error("Status Code:", jqXHR.status);
        }
    });
}

function notify(className, content){
    $("#notify").removeClass();
    $('#notify').addClass("alert");
    $('#notify').addClass(className);
    $("#notify").css("padding", "10px");
    $('#notify').text(content).show();
    // Hide the notification after 5 seconds
    setTimeout(function() {
        $('#notify').hide();
    }, 10000);
}

async function loadAdsPackage() {
    try {
        const response = await fetch('/api/ads/ads-package');
        const data = await response.json();

        const adsTypesContainer = document.getElementById('ads-types');
        adsTypesContainer.innerHTML = ''; // Clear existing content

        data.forEach(placement => {
            placement.adsTypes.forEach(type => {
                const div = document.createElement('div');
                div.className = 'col-md-4 package-option';
                div.dataset.package = `${type.name}-${placement.adsPlacement.name}`;
                div.dataset.price = type.price + placement.adsPlacement.price;
                div.innerHTML = `
                    <div class="card">
                        <div class="card-body">
                            <h5>${type.name}</h5>
                            <p>Vị trí: ${placement.adsPlacement.name}</p>
                            <p>Loại: ${type.description}</p>
                            <p>Giá: ${(type.price + placement.adsPlacement.price).toLocaleString('vi-VN')} VND/ngày</p>
                        </div>
                    </div>
                `;
                adsTypesContainer.appendChild(div);
                div.addEventListener('click', function () {
                    adForm.setPackage(`${type.name}-${placement.adsPlacement.name}`);
                    adForm.setPrice(type.price + placement.adsPlacement.price);
                    $('#placementId').val(placement.adsPlacement.id);
                    $('#typeId').val(type.id);
                });
            });
        });
    } catch (error) {
        console.error("Error loading Ads Packages:", error);
    }
}

class AdvertisingForm {
    constructor() {
        this.package = '';
        this.title = '';
        this.content = '';
        this.startDate = '';
        this.endDate = '';
        this.price = 0;
        this.totalDays = 0;
        this.totalCost = 0;
    }

    setPackage(packageName) {
        this.package = packageName;
        this.updateCost();
        this.updatePackageUI();
        this.updateUI();
    }

    setPrice(price) {
        this.price = price;
        this.updateCost();
        this.updatePackageUI();
        this.updateUI();
    }

    setTitle(title) {
        this.title = title;
    }

    setContent(content) {
        this.content = content;
    }

    setStartDate(date) {
        this.startDate = date;
        this.updateTotalDays();
    }

    setEndDate(date) {
        this.endDate = date;
        this.updateTotalDays();
    }

    updateTotalDays() {
        if (this.startDate && this.endDate) {
            const start = new Date(this.startDate);
            const end = new Date(this.endDate);
            const diffTime = Math.abs(end - start);
            this.totalDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            this.updateCost();
        }
    }

    updateCost() {
        if (this.package && this.totalDays) {
            this.totalCost = this.price * this.totalDays;
        }
    }

    updatePackageUI() {
        document.querySelectorAll('.package-option').forEach(el => {
            el.classList.toggle('selected', el.dataset.package === this.package);
        });
    }

    updateUI() {
        document.getElementById('adTitle').value = this.title;
        document.getElementById('adContent').value = this.content;
        document.getElementById('startDate').value = this.startDate;
        document.getElementById('endDate').value = this.endDate;
        document.getElementById('totalDays').textContent = this.totalDays;
        document.getElementById('totalCost').textContent = this.totalCost.toLocaleString('vi-VN') + ' VND';

        this.updatePackageUI();
    }
}

const adForm = new AdvertisingForm();

document.getElementById('adTitle').addEventListener('input', function () {
    adForm.setTitle(this.value);
});

document.getElementById('adContent').addEventListener('input', function () {
    adForm.setContent(this.value);
});

document.getElementById('startDate').addEventListener('change', function () {
    adForm.setStartDate(this.value);
    adForm.updateUI();
});

document.getElementById('endDate').addEventListener('change', function () {
    adForm.setEndDate(this.value);
    adForm.updateUI();
});

document.getElementById('saveAd').addEventListener('click', function() {
    var modal = bootstrap.Modal.getInstance(document.getElementById('advertisingModal'));
    modal.hide();
    loadAds(1);
});