import {
  View,
  FlatList,
  RefreshControl,
  TouchableWithoutFeedback,
  TouchableOpacity,
} from "react-native";
import React, { useCallback, useEffect, useRef, useState } from "react";
import { useFocusEffect } from "@react-navigation/native";
import Skeleton from "../components/Skeleton";
import EmptyState from "../components/EmptyState";
import StreakFlame from "../components/StreakFlame";
import { Post as PostDomain } from "../utils/Post";
import Post from "../components/Post";
import Icon from "react-native-vector-icons/Ionicons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  getPostBySuscribedActivities,
  getPosts,
  getPostsFollowing,
} from "../repositories/PostRepository";
import {
  bgColor,
  borderColor,
  cardBgColor,
  iconColor,
  raisedBgColor,
} from "../utils/Utils";
import { palette } from "../styles/theme";
import { translations } from "../../translations/translation";
import { errorMessage } from "../utils/graphql";
import { useToast } from "../contexts/FeedbackContextProvider";

type Props = NativeStackScreenProps<HomeStackProps, "Home">;

type FeedType = "home" | "following" | "activity";

/** Feed tabs, General first since it is the one that opens. */
const FEED_TABS: { type: FeedType; icon: string }[] = [
  { type: "home", icon: "earth" },
  { type: "following", icon: "people" },
  { type: "activity", icon: "flame" },
];

const HomeScreen = ({ navigation }: Props) => {
  const [type, setType] = useState<FeedType>("home");
  const [load, setLoad] = useState<boolean>(false);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [posts, setPosts] = useState<PostDomain[]>([]);
  const { darkmode, language } = useSettingsContext();
  const toast = useToast();
  const lastDate = useRef(new Date().toISOString().slice(0, 19));
  const [initialLoading, setInitialLoading] = useState<boolean>(true);
  const focusedOnce = useRef(false);
  const t = translations[language || "en-EN"].screens.Home;

  // Returning to the feed (e.g. after commenting) refreshes counts quietly.
  useFocusEffect(
    useCallback(() => {
      if (!focusedOnce.current) {
        focusedOnce.current = true;
        return;
      }
      lastDate.current = new Date().toISOString().slice(0, 19);
      fetchPosts(false);
    }, [type])
  );

  useEffect(() => {
    fetchPosts();
  }, []);

  useEffect(() => {
    if (load || isLoadingMore) {
      fetchPosts();
    }
  }, [load, isLoadingMore]);

  useEffect(() => {
    setInitialLoading(true);
    reload();
  }, [type]);

  const fetchPosts = async (append: boolean = isLoadingMore) => {
    try {
      let response = [];
      switch (type) {
        case "following":
          response = await getPostsFollowing(lastDate.current);
          break;
        case "home":
          response = await getPosts(lastDate.current);
          break;
        case "activity":
          response = await getPostBySuscribedActivities(lastDate.current);
          break;
      }
      setPosts((previous) => {
        if (!append) return response;
        const seen = new Set(previous.map((p) => p.id));
        return [...previous, ...response.filter((p) => !seen.has(p.id))];
      });
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setLoad(false);
      setIsLoadingMore(false);
      setInitialLoading(false);
    }
  };

  // Each feed is empty for a different reason, so each offers its own way out.
  const tabs = () => navigation.getParent() as any;
  const emptyActions: Record<FeedType, () => void> = {
    home: () => tabs()?.navigate("UploadForm"),
    following: () => navigation.navigate("Search"),
    activity: () =>
      tabs()?.navigate("ActivitiesStackNavigation", { screen: "Activities" }),
  };

  const reload = () => {
    lastDate.current = new Date().toISOString().slice(0, 19);
    setLoad(true);
  };

  const loadMore = () => {
    if (isLoadingMore || posts.length === 0) return;
    lastDate.current = posts[posts.length - 1].createdAt.slice(0, 19);
    setIsLoadingMore(true);
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <View className="flex-row items-center px-5 pt-4 pb-4">
        <View
          className={`flex-1 flex-row p-1 rounded-xl ${raisedBgColor(darkmode)}`}
        >
          {FEED_TABS.map((tab) => {
            const isActive = type === tab.type;
            return (
              <TouchableOpacity
                key={tab.type}
                activeOpacity={0.7}
                onPress={() => setType(tab.type)}
                accessibilityRole="tab"
                accessibilityState={{ selected: isActive }}
                accessibilityLabel={t.type[tab.type]}
                className={`flex-1 h-9 items-center justify-center rounded-lg ${
                  isActive ? `${cardBgColor(darkmode)} border ${borderColor(darkmode)}` : ""
                }`}
              >
                <Icon
                  name={isActive ? tab.icon : `${tab.icon}-outline`}
                  size={20}
                  color={
                    isActive
                      ? darkmode
                        ? palette.accentOnDark
                        : palette.accent
                      : iconColor(darkmode)
                  }
                />
              </TouchableOpacity>
            );
          })}
        </View>
        <TouchableOpacity
          onPress={() => navigation.navigate("Search")}
          hitSlop={8}
          className="ml-4"
        >
          <Icon name="search" size={22} color={iconColor(darkmode)} />
        </TouchableOpacity>
      </View>
      <View className="flex-1">
        <FlatList
          refreshControl={
            <RefreshControl refreshing={load} onRefresh={reload} />
          }
          data={posts}
          renderItem={({ item }) => {
            return <Post post={item} navigation={navigation} />;
          }}
          keyExtractor={(item) => item.id}
          contentContainerStyle={{ flexGrow: 1 }}
          ListEmptyComponent={
            initialLoading ? (
              <View className="px-5">
                {[0, 1].map((i) => (
                  <View key={i} className="mb-5">
                    <View className="flex-row items-center mb-3">
                      <Skeleton width={36} height={36} radius={18} />
                      <View className="ml-3 flex-1">
                        <Skeleton width="40%" height={12} />
                        <Skeleton width="25%" height={10} style={{ marginTop: 6 }} />
                      </View>
                    </View>
                    <Skeleton height={360} radius={16} />
                  </View>
                ))}
              </View>
            ) : (
              <EmptyState
                visual={<StreakFlame streak={0} size={44} />}
                title={t.empty[type].title}
                hint={t.empty[type].hint}
                actionLabel={t.empty[type].action}
                onAction={emptyActions[type]}
              />
            )
          }
          onEndReached={loadMore}
          onEndReachedThreshold={0.5}
        />
      </View>
    </View>
  );
};

export default HomeScreen;
