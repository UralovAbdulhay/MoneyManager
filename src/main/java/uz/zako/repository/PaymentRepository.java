package uz.zako.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.zako.entity.Category;
import uz.zako.entity.Payment;
import uz.zako.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    public boolean deleteByIdAndUsers(UUID id, User user);

    public Optional<Payment> findByIdAndUsers(UUID id, User user);

    public List<Payment> findAllByUsers(User user);

    public List<Payment> findAllByUsersAndCategory(User user, Category category);

    public List<Payment> findAllByUsersAndCategoryAndIncome(User user, Category category, boolean income);

    boolean existsByIdAndActive(UUID id, Boolean active);


}
