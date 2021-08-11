package com.shopme.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.entity.Country;
import com.shopme.entity.ShippingRate;

@Service
@Transactional
public class ShippingRateService {
	public static final int RATES_PER_PAGE = 10;

	@Autowired
	private ShippingRateRepositoty shipRepo;

	@Autowired
	private CountryRepository countryRepo;

	public Page<ShippingRate> listByPage(int pageNum, String keyword) {
		Pageable pageable = PageRequest.of(pageNum - 1, RATES_PER_PAGE);

		if (keyword != null) {
			return shipRepo.findAll(keyword, pageable);
		}

		return shipRepo.findAll(pageable);
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDB = shipRepo.findByCountryAndState(rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null
				&& !rateInDB.equals(rateInForm);

		if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("There's already a rate for the destination "
					+ rateInForm.getCountry().getName() + ", " + rateInForm.getState());
		}
		shipRepo.save(rateInForm);
	}

	public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
	}

	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}

		shipRepo.updateCODSupport(id, codSupported);
	}

	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);

		}
		shipRepo.deleteById(id);
	}
}
