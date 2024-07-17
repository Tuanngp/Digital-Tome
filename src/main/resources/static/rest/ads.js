$(document).ready(function () {
    let currentPage = 1;
    let isLoading = false;

    // Function to format date as "dd-MM-yyyy"
    function formatDate(date) {
        const d = new Date(date);
        const day = ('0' + d.getDate()).slice(-2);
        const month = ('0' + (d.getMonth() + 1)).slice(-2);
        const year = d.getFullYear();
        return `${day}-${month}-${year}`;
    }

    // Function to load books and pagination
    function loadAds(page) {
        if (isLoading) return;
        isLoading = true;
        $.ajax({
            url: '/api/ads',
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                $('#booksList').empty();
                data.forEach(function (ads, index) {
                    const row = $('<tr>');
                    row.append('<td><div class="form-check custom-checkbox checkbox-success check-lg me-3">' +
                        '<input type="checkbox" class="form-check-input" id="customCheckBox' + index + '" required="">' +
                        '<label class="form-check-label" for="customCheckBox' + index + '"></label></div></td>');
                    row.append('<td><strong>' + (ads.id || 'N/A') + '</strong></td>');
                    row.append('<td id="adsName" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 100px;">' + (ads.title || 'N/A') + '</td>');
                    row.append('<td><img src="' + (ads.imageUrl || '') + '" alt="image" style="max-height: 100px;"></td>');
                    row.append('<td id="adsType">' + (ads.type || 'N/A') + '</td>');
                    row.append('<td id="startDate">' + (ads.startDate ? formatDate(ads.startDate) : 'N/A') + '</td>');
                    row.append('<td id="endDate">' + (ads.endDate ? formatDate(ads.endDate) : 'N/A') + '</td>');
                    row.append('<td><div class="d-flex align-items-center"><i class="fa fa-circle text-warning me-1"></i> ' + ads.status + '</div></td>');
                    row.append('<td><div class="d-flex">' +
                        '<button id="acceptBtn" class="btn btn-success shadow btn-md sharp me-1">Accept</button>' +
                        '<button id="rejectBtn" class="btn btn-danger shadow btn-md sharp me-1">Reject</button>' +
                        '</div></td>');

                    $('#adsList').append(row);
                });

                // Pagination setup
                $('#pagination').twbsPagination('destroy');
                $('#pagination').twbsPagination({
                    totalPages: data.totalPages,
                    startPage: data.currentPage,
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

                isLoading = false;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error loading Ads:", textStatus, errorThrown);
                isLoading = false;
            }
        });
    }

    function loadAdsTypes() {
        $.ajax({
            url: '/api/ads/types',
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                $('#adsType').empty();
                data.forEach(function (type) {
                    $('#adsType').append('<div class="col-md-3 package-option" data-package="Gói Cơ Bản">\n' +
                        '                                    <div class="card">\n' +
                        '                                        <div class="card-body">\n' +
                        '                                            <h5></h5>\n' +
                        '                                            <p>Vị trí: Sidebar</p>\n' +
                        '                                            <p>Loại: Banner</p>\n' +
                        '                                            <p>Giá: 500,000 VND/ngày</p>\n' +
                        '                                        </div>\n' +
                        '                                    </div>\n' +
                        '                                </div>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error loading Ads Types:", textStatus, errorThrown);
            }
        });
    }

    $('#url-update-avatar').on('change', function () {
        showImgAvatar(this);
    });

    function showImgAvatar(selectFile) {
        let file = selectFile.files[0];
        let reader = new FileReader();

        reader.onload = function () {
            $('#image-preview').attr('src', reader.result);
        };

        if (file) {
            reader.readAsDataURL(file);
        }
    }

    // Initial load of books
    loadAds(1);
});

const adForm = new AdvertisingForm();

document.addEventListener('DOMContentLoaded', function () {
    // Khôi phục trạng thái đã chọn nếu có
    if (adForm.package) {
        adForm.updatePackageUI();
    }
});

document.querySelectorAll('.package-option').forEach(option => {
    option.addEventListener('click', function () {
        const packageName = this.dataset.package;
        adForm.setPackage(packageName);
    });
});

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

document.getElementById('saveAd').addEventListener('click', function () {
    console.log('Quảng cáo đã được lưu:', adForm);
    // Thêm logic để lưu quảng cáo vào cơ sở dữ liệu hoặc gửi đến server
});

class AdvertisingForm {
    constructor() {
        this.package = '';
        this.title = '';
        this.content = '';
        this.startDate = '';
        this.endDate = '';
        this.totalDays = 0;
        this.totalCost = 0;
    }

    setPackage(packageName) {
        this.package = packageName;
        this.updateCost();
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
        const prices = {
            'Gói Cơ Bản': 500000,
            'Gói Tiêu Chuẩn': 700000,
            'Gói Cao Cấp': 1000000,
            'Gói Video': 1500000,
            'Gói Pop-up': 2000000
        };
        if (this.package && this.totalDays) {
            this.totalCost = prices[this.package] * this.totalDays;
        }
    }

    updatePackageUI() {
        document.querySelectorAll('.package-option').forEach(el => {
            if (el.dataset.package === this.package) {
                el.classList.add('selected');
            } else {
                el.classList.remove('selected');
            }
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
