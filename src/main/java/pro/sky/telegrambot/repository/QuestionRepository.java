package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Question;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "select * from question where is_answered = false and volunteer_chat_id is null order by id LIMIT 1", nativeQuery = true)
    Question getOlderQuestion();

    @Query(value = "select * from question where chat_id = ?1 and is_asked = false order by id desc LIMIT 1", nativeQuery = true)
    Question getLastQuestion(long chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE question SET question = ?2, is_asked = true WHERE chat_id = ?1 and is_asked = false", nativeQuery = true)
    void createNewQuestion(long chatId, String question);

    @Transactional
    @Modifying
    @Query(value = "UPDATE question SET volunteer_chat_id = ?2 WHERE id = ?1", nativeQuery = true)
    void markQuestionAsTaken(long id, long volunteer_chat_id);

    @Query(value = "select * from question where volunteer_chat_id = ?1 and is_answered = false order by id desc LIMIT 1", nativeQuery = true)
    Question getQuestionForAnswer(long chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE question SET is_answered = true WHERE id = ?1", nativeQuery = true)
    void markQuestionAsAnswered(long id);

}
