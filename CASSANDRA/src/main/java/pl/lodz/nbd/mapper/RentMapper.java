package pl.lodz.nbd.mapper;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import pl.lodz.nbd.dao.RentDao;

@Mapper
public interface RentMapper {

    @DaoFactory
    RentDao rentDao();
}
