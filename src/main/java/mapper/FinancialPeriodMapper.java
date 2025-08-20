package mapper;

import com.example.spring.dto.company.financial.FinancialPeriod;
import com.example.spring.dto.company.financial.FinancialPeriodDTO;
import com.example.spring.dto.company.financial.FinancialYear;
import com.example.spring.model.Company;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinancialPeriodMapper {

    public static List<FinancialPeriodDTO> toFinancialPeriodDTOList(Company company) {
        List<FinancialPeriodDTO> periods = new ArrayList<>();

        for (FinancialYear year : FinancialYear.values()) {
            String yearStr = year.name().substring(1); // e.g. Y2018 -> 2018

            for (FinancialPeriod period : FinancialPeriod.values()) {
                String periodStr = period.name().substring(1); // e.g. P1 -> 1

                LocalDate closingDate = (LocalDate) invokeGetter(company, "getClosingDate_" + yearStr + "_" + periodStr);
                Double revenue = (Double) invokeGetter(company, "getRevenue_" + yearStr + "_" + periodStr);
                Double turnover = (Double) invokeGetter(company, "getTurnover_" + yearStr + "_" + periodStr);

                FinancialPeriodDTO dto = FinancialPeriodDTO.builder()
                        .year(year)
                        .period(period)
                        .closingDate(closingDate)
                        .revenue(revenue)
                        .turnover(turnover)
                        .build();

                periods.add(dto);
            }
        }

        validatePeriods(periods);
        return periods;
    }

    private static Object invokeGetter(Company company, String methodName) {
        try {
            Method method = company.getClass().getMethod(methodName);
            return method.invoke(company);
        } catch (NoSuchMethodException e) {
            // Method not found, return null (means data missing)
            return null;
        } catch (Exception e) {
            // Other reflection errors
            throw new RuntimeException("Failed to invoke method: " + methodName, e);
        }
    }

    private static void validatePeriods(List<FinancialPeriodDTO> periods) {
        boolean hasValidPeriod = false;

        for (FinancialPeriodDTO period : periods) {
            if (period.getClosingDate() != null &&
                    period.getRevenue() != null &&
                    period.getTurnover() != null) {
                hasValidPeriod = true;
                break;
            }
        }

        if (!hasValidPeriod) {
            throw new IllegalStateException("All financial periods for all years are missing data");
        }
    }
}
