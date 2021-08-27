package cucumbertables;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class MapRowWrapObjectTest {
    @Test
    void convertTo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "cbk");
        map.put("age", "10");
        map.put("rate1", "10.3");
        map.put("rate2", "");
        map.put("birth", "2021-06-11");
        MapRowWrap row = new MapRowWrap(map, "<null>");
        Mem mem = row.convertTo(Mem.class);
        assertThat(mem.getName()).isEqualTo("cbk");
        assertThat(mem.getAge()).isEqualTo(10);
        assertThat(mem.getRate1()).isEqualTo(10.3);
        assertThat(mem.getRate2()).isNull();
        assertThat(mem.getBirth()).isEqualTo(LocalDate.of(2021, 6, 11));
    }

    @Test
    void copyTo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "cbk");
        map.put("age", "10");
        map.put("rate2", "1.1");
        map.put("birth", "2021-06-11");
        MapRowWrap row = new MapRowWrap(map);

        Mem mem = new Mem();
        mem.setName("before");
        mem.setRate1(0.0);

        row.copyTo(mem);
        assertThat(mem.getName()).isEqualTo("cbk");
        assertThat(mem.getAge()).isEqualTo(10);
        assertThat(mem.getRate1()).isEqualTo(0.0);
        assertThat(mem.getRate2()).isEqualTo(1.1f);
        assertThat(mem.getBirth()).isEqualTo(LocalDate.of(2021, 6, 11));
    }

    @Test
    void exceptionNotMatchingName() {
        Assertions.assertThatThrownBy(() ->
        {
            HashMap<String, String> map = new HashMap<>();
            map.put("name1", "cbk");
            MapRowWrap row = new MapRowWrap(map);
            row.convertTo(Mem.class, true);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name1");
    }

    public static class Mem {
        private String name;
        private int age;
        private Double rate1;
        private Float rate2;
        private LocalDate birth;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Double getRate1() {
            return rate1;
        }

        public void setRate1(Double rate1) {
            this.rate1 = rate1;
        }

        public Float getRate2() {
            return rate2;
        }

        public void setRate2(Float rate2) {
            this.rate2 = rate2;
        }

        public LocalDate getBirth() {
            return birth;
        }

        public void setBirth(LocalDate birth) {
            this.birth = birth;
        }
    }
}
